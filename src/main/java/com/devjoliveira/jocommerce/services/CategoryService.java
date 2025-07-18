package com.devjoliveira.jocommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.CategoryDto;
import com.devjoliveira.jocommerce.repositories.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public List<CategoryDto> findAll() {

    List<CategoryDto> categoryfromDB = categoryRepository.findAll()
        .stream()
        .map(CategoryDto::new)
        .collect(Collectors.toList());

    return categoryfromDB;
  }

}
