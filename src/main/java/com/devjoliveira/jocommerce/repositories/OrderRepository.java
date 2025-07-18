package com.devjoliveira.jocommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
