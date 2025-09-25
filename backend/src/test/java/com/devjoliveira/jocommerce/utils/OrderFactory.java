package com.devjoliveira.jocommerce.utils;

import java.time.Instant;

import com.devjoliveira.jocommerce.entities.Order;
import com.devjoliveira.jocommerce.entities.OrderItem;
import com.devjoliveira.jocommerce.entities.Payment;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.enums.OrderStatus;

public class OrderFactory {

  public static Order createOrderFactory(User client) {

    Order order = new Order(1L, Instant.now(),
        OrderStatus.WAITING_PAYMENT, client, new Payment());

    Product product = ProductFactory.createProduct();

    OrderItem orderItem = new OrderItem(order, product, 1, 999.0);

    order.getItems().add(orderItem);

    return order;
  }
}
