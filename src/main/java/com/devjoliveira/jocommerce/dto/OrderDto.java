package com.devjoliveira.jocommerce.dto;

import java.time.Instant;
import java.util.List;

import com.devjoliveira.jocommerce.entities.Order;

import jakarta.validation.constraints.NotEmpty;

public record OrderDto(
    Long id,
    Instant moment,
    String status,
    UserMinDto userMin,
    PaymentDto payment,
    @NotEmpty(message = "Order must have at least one item") List<OrderItemDto> items

) {

  public OrderDto(Order order) {
    this(
        order.getId(),
        order.getMoment(),
        order.getStatus().name(),
        new UserMinDto(order.getClient()),
        order.getPayment() != null ? new PaymentDto(order.getPayment()) : null,
        order.getItems().stream().map(OrderItemDto::new).toList());

  }

  public Double getTotal() {
    Double sum = 0.0;
    for (OrderItemDto item : items) {
      sum += item.getSubTotal();
    }
    return sum;
  }

}
