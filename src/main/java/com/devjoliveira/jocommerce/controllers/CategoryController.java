package com.devjoliveira.jocommerce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjoliveira.jocommerce.dto.CategoryDto;
import com.devjoliveira.jocommerce.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryDto>> findAll() {
    return ResponseEntity.ok().body(categoryService.findAll());
  }

}
