package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.Product;

public record ProductDto(
    Long id,
    String name,
    String description,
    Double price,
    String imageUrl) {

  public ProductDto(Product product) {
    this(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getImageUrl());
  }
}
