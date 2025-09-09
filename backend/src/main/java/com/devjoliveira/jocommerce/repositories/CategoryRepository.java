package com.devjoliveira.jocommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
