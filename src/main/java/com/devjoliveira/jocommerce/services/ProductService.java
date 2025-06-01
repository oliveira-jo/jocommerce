package com.devjoliveira.jocommerce.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public ProductDto findById(Long id) {

    Optional<Product> productFromDB = productRepository.findById(id);
    if (!productFromDB.isPresent()) {
      throw new RuntimeException("Product not found with ID: " + id);
    }

    return new ProductDto(productFromDB.get());

  }

  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(Pageable pageable) {

    Page<Product> productFromDB = productRepository.findAll(pageable);

    return productFromDB.map(ProductDto::new);

  }

  @Transactional
  public ProductDto insert(ProductDto productDto) {

    Product product = new Product();
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    product.setImageUrl(productDto.imageUrl());

    return new ProductDto(productRepository.save(product));

  }
}
