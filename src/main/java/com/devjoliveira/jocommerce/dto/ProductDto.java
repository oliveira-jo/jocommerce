package com.devjoliveira.jocommerce.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.devjoliveira.jocommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDto(

    Long id,

    @NotBlank(message = "Name is required") @Size(min = 3, max = 80, message = "Name must be between 3 than 100 characters") String name,

    @NotBlank(message = "Description is required") @Size(min = 10, message = "Description must be at least 10 characters") String description,

    @NotNull(message = "Price is required") @Positive(message = "Price must be positive") Double price,

    @NotEmpty(message = "At least one category is required") List<CategoryDto> categories,

    String imageUrl) {

  public ProductDto(Product product) {
    this(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getCategories().stream()
            .map(obj -> new CategoryDto(obj.getId(), obj.getName()))
            .collect(Collectors.toList()),
        product.getImageUrl());
  }

}
