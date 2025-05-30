package com.devjoliveira.jocommerce.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductDto findById(Long id) {

    Optional<Product> productFromDB = productRepository.findById(id);
    if (!productFromDB.isPresent()) {
      throw new RuntimeException("Product not found with ID: " + id);
    }

    return new ProductDto(productFromDB.get());

  }
}
