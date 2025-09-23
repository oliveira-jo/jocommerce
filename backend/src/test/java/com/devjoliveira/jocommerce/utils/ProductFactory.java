package com.devjoliveira.jocommerce.utils;

import com.devjoliveira.jocommerce.entities.Category;
import com.devjoliveira.jocommerce.entities.Product;

public class ProductFactory {

  public static Product createProduct() {

    Category category = CategoryFactory.createCategory();

    Product product = new Product(1L, "Smart TV", "A TV with smart features", 1999.99, "https://example.com/tv.jpg");
    product.getCategories().add(category);

    return product;
  }

  public static Product createProduct(String name) {
    Product product = createProduct();
    product.setName(name);
    return product;
  }

}
