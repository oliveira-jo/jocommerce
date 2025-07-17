package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.Category;

public record CategoryDto(

    Long id,

    String name) {

  public CategoryDto(Category category) {
    this(
        category.getId(),
        category.getName());
  }

}
