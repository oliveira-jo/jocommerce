package com.devjoliveira.jocommerce.services;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.ProductCategoryDto;
import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.dto.ProductMinDto;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.Exceptions.DatabaseException;
import com.devjoliveira.jocommerce.services.Exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
  public Page<ProductMinDto> findAll(String name, Pageable pageable) {
    Page<Product> productFromDB = productRepository.searchByName(name, pageable);
    return productFromDB.map(ProductMinDto::new);
  }

  @Transactional(readOnly = true)
  public Page<ProductCategoryDto> find(Pageable pageable) {
    Page<Product> page = productRepository.findAll(pageable);
    /*
     * CHAMADA SECA
     * 
     * JPA guarda em memória os objetos que já foram carregados
     * e não faz nova consulta ao banco de dados.
     * Isso é chamado de "Mapa de Identidade".
     * 
     * Neste caso estamos guardando as categorias em memória para não fazer nova
     * consulta
     * ao banco de dados quando acessarmos as categorias de cada produto.
     * Isso é feito para evitar o problema de N+1 consultas.
     */
    productRepository.searchProductsCategories(page.stream().collect(Collectors.toList()));

    return page.map(ProductCategoryDto::new);
  }

  @Transactional
  public ProductDto insert(ProductDto dto) {
    Product entity = new Product();
    copyDtoToEntity(dto, entity);
    return new ProductDto(productRepository.save(entity));

  }

  @Transactional
  public ProductDto update(Long id, ProductDto dto) {
    try {

      Product entity = productRepository.getReferenceById(id);
      copyDtoToEntity(dto, entity);
      return new ProductDto(productRepository.save(entity));

    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Resourse not found");
    }

  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFoundException("Resourse not found");
    }

    try {
      productRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Fail in integrity references");
    }

  }

  private void copyDtoToEntity(ProductDto productDto, Product product) {
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    product.setImageUrl(productDto.imageUrl());
  }
}
