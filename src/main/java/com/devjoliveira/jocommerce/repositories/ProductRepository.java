package com.devjoliveira.jocommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devjoliveira.jocommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))")
  Page<Product> searchByName(String name, Pageable pageable);

  @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p IN :products")
  List<Product> searchProductsCategories(List<Product> products);

}
