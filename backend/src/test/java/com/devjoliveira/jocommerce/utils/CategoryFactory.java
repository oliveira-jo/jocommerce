package com.devjoliveira.jocommerce.utils;

import com.devjoliveira.jocommerce.entities.Category;

public class CategoryFactory {

  public static Category createCategory() {
    return new Category(1L, "Electronics");
  }

  public static Category createCategory(Long id, String name) {
    return new Category(id, name);
  }

}
