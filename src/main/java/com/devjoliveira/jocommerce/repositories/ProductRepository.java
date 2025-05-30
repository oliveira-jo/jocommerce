package com.devjoliveira.jocommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
