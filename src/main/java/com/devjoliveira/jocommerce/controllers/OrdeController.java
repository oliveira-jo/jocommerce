package com.devjoliveira.jocommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrdeController {

  private final OrderService orderService;

  public OrdeController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

}
