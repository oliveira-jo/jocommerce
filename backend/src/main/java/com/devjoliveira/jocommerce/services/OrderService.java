package com.devjoliveira.jocommerce.services;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.dto.OrderItemDto;
import com.devjoliveira.jocommerce.entities.Order;
import com.devjoliveira.jocommerce.entities.OrderItem;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.enums.OrderStatus;
import com.devjoliveira.jocommerce.repositories.OrderItemRepository;
import com.devjoliveira.jocommerce.repositories.OrderRepository;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  private final UserService userService;
  private final AuthService authService;

  public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
      ProductRepository productRepository, UserService userService, AuthService authService) {
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
    this.productRepository = productRepository;
    this.userService = userService;
    this.authService = authService;
  }

  @Transactional(readOnly = true)
  public OrderDto findById(Long id) {

    Order entity = orderRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource not found"));

    // Validate if the user is the owner of the order or an admin
    authService.validateSelfOrAdmin(entity.getClient().getId());

    return new OrderDto(entity);
  }

  @Transactional
  public OrderDto insert(OrderDto orderDto) {

    Order order = new Order();

    order.setMoment(Instant.now());
    order.setStatus(OrderStatus.WAITING_PAYMENT);

    User user = userService.authenticated();
    order.setClient(user);

    for (OrderItemDto itemDto : orderDto.items()) {

      Product product = productRepository.getReferenceById(itemDto.productId());

      OrderItem orderItem = new OrderItem(order, product, itemDto.quantity(), product.getPrice());

      order.getItems().add(orderItem);

    }

    orderRepository.save(order);
    orderItemRepository.saveAll(order.getItems());

    return new OrderDto(order);

  }

}
