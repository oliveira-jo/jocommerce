package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.Product;

public record ProductMinDto(

    Long id,

    String name,

    Double price,

    String imageUrl) {

  public ProductMinDto(Product product) {
    this(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImageUrl());
  }

}
