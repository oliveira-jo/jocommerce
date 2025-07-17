package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDto(

    Long id,

    @NotBlank(message = "Name is required") @Size(min = 3, max = 80, message = "Name must be between 3 than 100 characters") String name,

    @NotBlank(message = "Description is required") @Size(min = 10, message = "Description must be at least 10 characters") String description,

    @Positive(message = "Price must be positive") Double price,

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
