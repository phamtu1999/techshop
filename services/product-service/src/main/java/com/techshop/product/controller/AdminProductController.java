package com.techshop.product.controller;

import com.techshop.product.dto.response.ApiResponse;
import com.techshop.product.dto.response.ProductResponse;
import com.techshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        if (productService == null) return null;
        return ApiResponse.success(null);
    }

    @PostMapping
    public ApiResponse<ProductResponse> createProduct() {
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable UUID id) {
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable UUID id) {
        return ApiResponse.success(null);
    }
    
    @PostMapping("/{id}/images")
    public ApiResponse<Void> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "isThumbnail", defaultValue = "false") boolean isThumbnail) {
        productService.uploadProductImage(id, file, isThumbnail);
        return ApiResponse.success(null);
    }
}
