package com.devjoliveira.jocommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocommerce.dto.CategoryDto;
import com.devjoliveira.jocommerce.entities.Category;
import com.devjoliveira.jocommerce.repositories.CategoryRepository;
import com.devjoliveira.jocommerce.utils.CategoryFactory;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

  @InjectMocks
  CategoryService service;

  @Mock
  CategoryRepository repository;

  private Category category;
  private List<Category> list;

  @BeforeEach
  void setUp() throws Exception {

    category = CategoryFactory.createCategory();

    list = new ArrayList<>();
    list.add(category);

    Mockito.when(repository.findAll()).thenReturn(list);

  }

  @Test
  public void findAll_ShouldReturnListOfCategoryDTO_WhenAllOkay() {

    List<CategoryDto> result = service.findAll();

    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(result.size(), 1);
    Assertions.assertEquals(result.get(0).id(), category.getId());
    Assertions.assertEquals(result.get(0).name(), category.getName());

  }

}
