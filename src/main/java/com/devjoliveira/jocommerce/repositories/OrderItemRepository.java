package com.devjoliveira.jocommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocommerce.entities.OrderItem;
import com.devjoliveira.jocommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
