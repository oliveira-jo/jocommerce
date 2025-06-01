package com.devjoliveira.jocommerce.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjoliveira.jocommerce.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("{id}")
  public ResponseEntity<?> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(productService.findById(id));
  }

  @GetMapping
  public ResponseEntity<?> findAll(Pageable pageable) {
    return ResponseEntity.ok().body(productService.findAll(pageable));
  }

}
