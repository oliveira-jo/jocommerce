package com.devjoliveira.jocommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocommerce.dto.CategoryDto;
import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.dto.ProductMinDto;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.repositories.CategoryRepository;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.exceptions.DatabaseException;
import com.devjoliveira.jocommerce.services.exceptions.ResourceNotFoundException;
import com.devjoliveira.jocommerce.utils.CategoryFactory;
import com.devjoliveira.jocommerce.utils.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

  @InjectMocks
  ProductService service;

  @Mock
  ProductRepository repository;

  @Mock
  CategoryRepository categoryRepository;

  private Long existingProductId;
  private Long nonExistingProductId;
  private Long dependentProductId;

  private Product product;
  private ProductDto productDto;
  private String productName;

  private PageImpl<Product> page;

  @BeforeEach
  void setUp() throws Exception {

    existingProductId = 1L;
    nonExistingProductId = 1000L;
    dependentProductId = 3L;
    productName = "Smart TV";

    product = ProductFactory.createProduct();
    productDto = new ProductDto(product);
    page = new PageImpl<>(List.of(product));

    Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
    Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());

    Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);

    Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);

    Mockito.when(repository.save(any())).thenReturn(product);

    Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(CategoryFactory.createCategory()));
    Mockito.when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

    Mockito.when(repository.getReferenceById(existingProductId)).thenReturn(product);
    Mockito.when(repository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

    Mockito.when(repository.existsById(existingProductId)).thenReturn(true);
    Mockito.when(repository.existsById(dependentProductId)).thenReturn(true);
    Mockito.when(repository.existsById(nonExistingProductId)).thenReturn(false);

    Mockito.doNothing().when(repository).deleteById(existingProductId);
    Mockito.doThrow(DataIntegrityViolationException.class)
        .when(repository).deleteById(dependentProductId);

  }

  @Test
  public void findById_ShouldReturnProductDTO_WhenIdExists() {

    ProductDto result = service.findById(existingProductId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.id(), existingProductId);
    Assertions.assertEquals(result.name(), productName);

  }

  @Test
  public void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.findById(nonExistingProductId);
    });

  }

  @Test
  public void findAllByName_ShouldReturnPageOfProductMinDTO_WhenAllOkay() {

    Pageable pageable = PageRequest.of(0, 12);

    Page<ProductMinDto> result = service.findAllByName(productName, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(1, result.getTotalElements());
    Assertions.assertEquals(productName, result.getContent().get(0).name());

  }

  @Test
  public void find_ShouldReturnPageOfProductDTO_WhenAllOkay() {

    Pageable pageable = PageRequest.of(0, 12);

    Page<ProductDto> result = service.findAll(pageable);

    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(1, result.getTotalElements());
    Assertions.assertEquals(productName, result.getContent().get(0).name());

  }

  @Test
  public void insert_ShouldReturnProductDTO_WhenValidDatas() {

    ProductDto result = service.insert(productDto);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(existingProductId, result.id());
    Assertions.assertEquals(productName, result.name());

  }

  @Test
  void insert_ShouldReturnResourceNotFound_WhenCategoryIdIsInvalid() {

    Mockito.when(categoryRepository.findById(any())).thenThrow(ResourceNotFoundException.class);

    productDto.categories().clear();
    productDto.categories().add(new CategoryDto(nonExistingProductId, ""));

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.insert(productDto);
    });

  }

  @Test
  public void update_ShouldReturnProductDTO_WhenIdExists() {

    String updateName = "New Name";
    ProductDto updatedProductDto = new ProductDto(existingProductId, updateName, "Updated Description", 800.0,
        List.of(new CategoryDto(1L, "Electronics")), "https://example.com/updated-image.jpg");

    ProductDto result = service.update(existingProductId, updatedProductDto);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.id(), existingProductId);
    Assertions.assertEquals(result.name(), updateName);

  }

  @Test
  void update_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.update(nonExistingProductId, productDto);
    });

  }

  @Test
  void delete_ShouldDoNothing_WhenIdExists() {

    Assertions.assertDoesNotThrow(() -> {
      service.delete(existingProductId);
    });

  }

  @Test
  void delete_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.delete(nonExistingProductId);
    });

  }

  @Test
  public void delete_ShouldThrowDatabaseException_WhenIdIsDependent() {

    Assertions.assertThrows(DatabaseException.class, () -> {
      service.delete(dependentProductId);
    });

  }

  @Test
  public void copyDtoToEntity_ShouldCopyData_WhenAllOkay() {

    Product newProductEntity = new Product();

    service.copyDtoToEntity(productDto, newProductEntity);

    Assertions.assertEquals(productDto.name(), newProductEntity.getName());
    Assertions.assertEquals(productDto.description(), newProductEntity.getDescription());
    Assertions.assertEquals(productDto.price(), newProductEntity.getPrice());
    Assertions.assertEquals(productDto.imageUrl(), newProductEntity.getImageUrl());

    Assertions.assertEquals(productDto.categories().get(0).id(),
        newProductEntity.getCategories().iterator().next().getId());

    Assertions.assertFalse(newProductEntity.getCategories().isEmpty());
    Assertions.assertEquals(1, newProductEntity.getCategories().size());

  }

  @Test
  public void copyDtoToEntity_ShouldThrowResourceNotFound_WhenCategoryIdDoesNotExists() {

    Product newProductEntity = new Product();
    productDto.categories().clear();
    productDto.categories().add(new CategoryDto(999L, "Non Existing Category"));

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.copyDtoToEntity(productDto, newProductEntity);
    });

  }

}
