package com.devjoliveira.jocommerce.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocommerce.entities.Product;

public record ProductCategoryDto(

    Long id,

    String name,

    String description,

    Double price,

    String imageUrl,

    Set<CategoryDto> categories) {

  public ProductCategoryDto(Product product) {
    this(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getImageUrl(),
        product.getCategories().stream().map(x -> new CategoryDto(x))
            .collect(Collectors.toSet()));
  }

}
