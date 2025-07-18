package com.devjoliveira.jocommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.entities.Order;
import com.devjoliveira.jocommerce.repositories.OrderRepository;
import com.devjoliveira.jocommerce.services.Exceptions.ResourceNotFoundException;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Transactional(readOnly = true)
  public OrderDto findById(Long id) {
    Order entity = orderRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource not found"));
    return new OrderDto(entity);
  }

}
