package com.devjoliveira.jocommerce.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.CategoryDto;
import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.dto.ProductMinDto;
import com.devjoliveira.jocommerce.entities.Category;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.CategoryRepository;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.exceptions.DatabaseException;
import com.devjoliveira.jocommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public ProductDto findById(Long id) {
    Product productFromDB = productRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource not found"));
    return new ProductDto(productFromDB);
  }

  @Transactional(readOnly = true)
  public Page<ProductMinDto> findAllByName(String name, Pageable pageable) {
    Page<Product> productFromDB = productRepository.searchByName(name, pageable);
    return productFromDB.map(ProductMinDto::new);
  }

  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(Pageable pageable) {
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

    return page.map(ProductDto::new);
  }

  @Transactional
  public ProductDto insert(ProductDto productDto) {

    Product newProductEntity = new Product();

    copyDtoToEntity(productDto, newProductEntity);

    Product fromDB = productRepository.save(newProductEntity);
    return new ProductDto(fromDB);
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

  protected void copyDtoToEntity(ProductDto productDto, Product product) {
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    product.setImageUrl(productDto.imageUrl());

    product.getCategories().clear();

    for (CategoryDto catDto : productDto.categories()) {

      Optional<Category> categoryFromDB = categoryRepository.findById(catDto.id());

      product.getCategories().add(categoryFromDB.orElseThrow(
          () -> new ResourceNotFoundException("Category not found: " + catDto.id())));
    }

  }
}
