package com.devjoliveira.jocommerce.controllers;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devjoliveira.jocommerce.dto.ProductDto;
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

  @PostMapping
  public ResponseEntity<?> insert(@RequestBody ProductDto productDto) {
    productDto = productService.insert(productDto);
    // good practice, return resource uri
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(productDto.id()).toUri();
    return ResponseEntity.created(uri).body(productDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
    return ResponseEntity.ok().body(productService.update(id, productDto));
  }

}
