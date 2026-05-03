package com.techshop.product.service.impl;

import com.techshop.product.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupabaseStorageServiceImpl implements StorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final WebClient.Builder webClientBuilder;

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) originalFilename = "file";
        
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        // Supabase Storage API endpoint for uploading: /storage/v1/object/{bucket}/{path}
        String uploadUrl = String.format("%s/storage/v1/object/%s/%s", supabaseUrl, bucketName, fileName);

        try {
            WebClient webClient = webClientBuilder.build();
            String contentType = file.getContentType();
            MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;

            webClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("apikey", supabaseKey)
                    .contentType(mediaType)
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // Return the public URL
            return String.format("%s/storage/v1/object/public/%s/%s", supabaseUrl, bucketName, fileName);
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            log.error("Supabase API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Supabase upload failed: " + e.getMessage());
        } catch (IOException e) {
            log.error("IO Error uploading file to Supabase", e);
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during file upload", e);
            throw new RuntimeException("Failed to upload image due to unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        // Parse fileName from URL and call DELETE endpoint if necessary
    }
}
