package com.devjoliveira.jocommerce.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.Exceptions.ResourceNotFoundException;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public ProductDto findById(Long id) {

    Product productFromDB = productRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource not found"));

    return new ProductDto(productFromDB);

  }

  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(Pageable pageable) {

    Page<Product> productFromDB = productRepository.findAll(pageable);

    return productFromDB.map(ProductDto::new);

  }

  @Transactional
  public ProductDto insert(ProductDto dto) {
    Product entity = new Product();
    copyDtoToEntity(dto, entity);
    return new ProductDto(productRepository.save(entity));

  }

  @Transactional
  public ProductDto update(Long id, ProductDto dto) {
    Product entity = productRepository.getReferenceById(id);
    copyDtoToEntity(dto, entity);
    return new ProductDto(productRepository.save(entity));

  }

  @Transactional
  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  private void copyDtoToEntity(ProductDto productDto, Product product) {
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    product.setImageUrl(productDto.imageUrl());
  }
}
