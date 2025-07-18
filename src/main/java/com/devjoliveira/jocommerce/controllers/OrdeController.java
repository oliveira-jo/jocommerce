package com.devjoliveira.jocommerce.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.services.OrderService;

import jakarta.validation.Valid;

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

  @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
  @PostMapping
  public ResponseEntity<OrderDto> insert(@Valid @RequestBody OrderDto orderDto) {
    orderDto = orderService.insert(orderDto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(orderDto.id()).toUri();
    return ResponseEntity.created(uri).body(orderDto);
  }

}
