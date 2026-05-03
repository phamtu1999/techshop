package com.techshop.product.mapper;

import com.techshop.product.dto.response.ProductResponse;
import com.techshop.product.dto.response.ProductSkuResponse;
import com.techshop.product.entity.Product;
import com.techshop.product.entity.ProductSku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "skus", ignore = true) // Will be mapped manually in service
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    ProductResponse toResponse(Product product);

    ProductSkuResponse toSkuResponse(ProductSku sku);
}
