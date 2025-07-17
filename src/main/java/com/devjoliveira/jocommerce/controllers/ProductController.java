package com.devjoliveira.jocommerce.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devjoliveira.jocommerce.dto.ProductCategoryDto;
import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("{id}")
  public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(productService.findById(id));
  }

  // http://localhost:8080/api/v1/products?size=12&page=0&sort=name,desc&name=mac
  @GetMapping
  public ResponseEntity<Page<ProductDto>> findAll(@RequestParam(name = "name", defaultValue = "") String name,
      Pageable pageable) {
    return ResponseEntity.ok().body(productService.findAll(name, pageable));
  }

  @GetMapping("/withcategories")
  public ResponseEntity<Page<ProductCategoryDto>> find(Pageable pageable) {
    Page<ProductCategoryDto> list = productService.find(pageable);
    return ResponseEntity.ok().body(list);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<ProductDto> insert(@Valid @RequestBody ProductDto productDto) {
    productDto = productService.insert(productDto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(productDto.id()).toUri();
    return ResponseEntity.created(uri).body(productDto);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
    return ResponseEntity.ok().body(productService.update(id, productDto));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
