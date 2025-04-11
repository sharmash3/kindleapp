package com.demo.kindle.catalog.mappers;

import com.demo.kindle.catalog.ProductDto;
import com.demo.kindle.catalog.domain.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto mapToDto(ProductEntity entity) {

        return new ProductDto(
                entity.getCode(), entity.getName(), entity.getDescription(), entity.getImageUrl(), entity.getPrice());
    }
}
