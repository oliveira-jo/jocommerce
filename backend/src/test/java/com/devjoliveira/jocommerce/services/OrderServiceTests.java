package com.devjoliveira.jocommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.entities.Order;
import com.devjoliveira.jocommerce.entities.OrderItem;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.repositories.OrderItemRepository;
import com.devjoliveira.jocommerce.repositories.OrderRepository;
import com.devjoliveira.jocommerce.repositories.ProductRepository;
import com.devjoliveira.jocommerce.services.exceptions.ForbiddenException;
import com.devjoliveira.jocommerce.services.exceptions.ResourceNotFoundException;
import com.devjoliveira.jocommerce.utils.OrderFactory;
import com.devjoliveira.jocommerce.utils.ProductFactory;
import com.devjoliveira.jocommerce.utils.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

  @InjectMocks
  OrderService orderService;

  @Mock
  OrderRepository orderRepository;

  @Mock
  OrderItemRepository orderItemRepository;

  @Mock
  ProductRepository productRepository;

  @Mock
  UserService userService;

  @Mock
  AuthService authService;

  private Long existingOrderId, nonExistingOrderId;
  private Long existingProductId, nonExistingProductId;

  private Order order;
  private OrderDto orderDto;

  private User userAdmin, userClient;
  private Product product;

  @BeforeEach
  void setUp() throws Exception {

    existingOrderId = 1L;
    nonExistingOrderId = 999L;

    existingProductId = 1L;
    nonExistingProductId = 999L;

    userAdmin = UserFactory.createCustonUserAdmin(1L, "Jef", "jef@gmail.com");
    userClient = UserFactory.createCustonUserClient(2L, "Bod", "bob@gmail.com");

    order = OrderFactory.createOrderFactory(userClient);
    orderDto = new OrderDto(order);

    product = ProductFactory.createProduct();

    Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
    Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

    Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
    Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

    Mockito.when(orderRepository.save(any())).thenReturn(order);
    Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

  }

  @Test
  public void findById_ShouldReturnOrderDto_WhenIdExistsAndAdminLogged() {

    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

    OrderDto result = orderService.findById(existingOrderId);

    Mockito.verify(orderRepository, Mockito.times(1)).findById(existingOrderId);
    Mockito.verify(authService, Mockito.times(1)).validateSelfOrAdmin(any());

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.id(), existingOrderId);

  }

  @Test
  public void findById_ShouldReturnOrderDto_WhenIdExistsAndSelfClientLogged() {

    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

    OrderDto result = orderService.findById(existingOrderId);

    Mockito.verify(orderRepository, Mockito.times(1)).findById(existingOrderId);
    Mockito.verify(authService, Mockito.times(1)).validateSelfOrAdmin(any());

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.id(), existingOrderId);

  }

  @Test
  public void findById_ShouldThrowForbiddenException_WhenIdExistsAndOtherClientLogged() {

    Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

    Assertions.assertThrows(ForbiddenException.class, () -> {
      orderService.findById(existingOrderId);
    });

  }

  @Test
  public void findById_ShouldThrowResourceNotFound_WhenIdIdDoesNotExist() {

    Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      orderService.findById(nonExistingOrderId);
    });

  }

  @Test
  public void insert_ShouldReturnOrderDto_WhenUserAdminLogged() {

    Mockito.when(userService.authenticated()).thenReturn(userAdmin);

    OrderDto result = orderService.insert(orderDto);

    Mockito.verify(orderRepository, Mockito.times(1)).save(any());
    Mockito.verify(orderItemRepository, Mockito.times(1)).saveAll(any());

    Assertions.assertNotNull(result);
    Assertions.assertEquals(existingOrderId, result.id());

  }

  @Test
  public void insert_ShouldReturnOrderDto_WhenUserClientLogged() {

    Mockito.when(userService.authenticated()).thenReturn(userClient);

    OrderDto result = orderService.insert(orderDto);

    Mockito.verify(orderRepository, Mockito.times(1)).save(any());
    Mockito.verify(orderItemRepository, Mockito.times(1)).saveAll(any());

    Assertions.assertNotNull(result);
    Assertions.assertEquals(existingOrderId, result.id());

  }

  @Test
  public void insert_ShouldThrowsUsernameNotFoundException_WhenUserNotLogged() {

    Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

    order.setClient(new User());
    orderDto = new OrderDto(order);

    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      orderService.insert(orderDto);
    });

    Mockito.verify(orderRepository, Mockito.times(0)).save(any());
    Mockito.verify(orderItemRepository, Mockito.times(0)).saveAll(any());

  }

  @Test
  public void insert_ShouldThrowsEntityNotFoundException_WhenProductIdDoesNotExist() {

    Mockito.when(userService.authenticated()).thenReturn(userClient);

    product.setId(nonExistingProductId);

    order.getItems().add(new OrderItem(order, product, 2, product.getPrice()));
    orderDto = new OrderDto(order);

    Assertions.assertThrows(EntityNotFoundException.class, () -> {
      orderService.insert(orderDto);
    });

  }

}
