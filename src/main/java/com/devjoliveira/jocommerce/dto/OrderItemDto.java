package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.OrderItem;

public record OrderItemDto(
    Long productId,
    String name,
    Double price,
    Integer quantity) {

  public OrderItemDto(OrderItem item) {
    this(
        item.getProduct().getId(),
        item.getProduct().getName(),
        item.getPrice(),
        item.getQuantity());
  }

  public Double getSubTotal() {
    return price * quantity;
  }

}
