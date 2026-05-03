package com.techshop.product.service.impl;

import com.techshop.product.dto.response.PagedResponse;
import com.techshop.product.dto.response.ProductResponse;
import com.techshop.product.entity.Product;
import com.techshop.product.mapper.ProductMapper;
import com.techshop.product.repository.ProductRepository;
import com.techshop.product.repository.ProductSkuRepository;
import com.techshop.product.service.ProductService;
import com.techshop.product.entity.ProductImage;
import com.techshop.product.repository.ProductImageRepository;
import com.techshop.product.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductImageRepository productImageRepository;
    private final StorageService storageService;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public void uploadProductImage(UUID productId, MultipartFile file, boolean isThumbnail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String imageUrl = storageService.uploadFile(file);

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .isThumbnail(isThumbnail)
                .build();

        productImageRepository.save(productImage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getProducts(String cursor, int limit) {
        UUID cursorUuid = (cursor != null && !cursor.isEmpty()) ? UUID.fromString(cursor) : null;
        Pageable pageable = PageRequest.of(0, limit + 1);
        
        List<Product> products = productRepository.findAllWithCursor(cursorUuid, pageable);
        
        boolean hasNext = products.size() > limit;
        List<Product> content = hasNext ? products.subList(0, limit) : products;
        
        String nextCursor = hasNext ? content.get(content.size() - 1).getId().toString() : null;
        
        List<ProductResponse> dtos = content.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
                
        return PagedResponse.<ProductResponse>builder()
                .content(dtos)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found with slug: " + slug));
        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = productMapper.toResponse(product);
        response.setSkus(productSkuRepository.findByProductId(product.getId()).stream()
                .map(productMapper::toSkuResponse)
                .collect(Collectors.toList()));
        
        List<ProductImage> images = productImageRepository.findByProductId(product.getId());
        response.setImages(images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList()));
        
        images.stream()
                .filter(ProductImage::isThumbnail)
                .findFirst()
                .ifPresent(img -> response.setThumbnail(img.getImageUrl()));
                
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> searchProducts(String query, int limit) {
        List<ProductResponse> products = productRepository.findByNameContainingIgnoreCase(query).stream()
                .limit(limit)
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

        return PagedResponse.<ProductResponse>builder()
                .content(products)
                .hasNext(false)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getRelatedProducts(@org.springframework.lang.NonNull UUID productId, int limit) {
        java.util.Objects.requireNonNull(productId, "productId must not be null");
        return productRepository.findById(productId)
                .map(product -> {
                    if (product.getCategory() == null) return List.<ProductResponse>of();
                    
                    return productRepository.findByCategoryIdAndIdNot(product.getCategory().getId(), productId).stream()
                            .limit(limit)
                            .map(this::mapToProductResponse)
                            .collect(Collectors.toList());
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
