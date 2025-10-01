package com.devjoliveira.jocommerce.controllersIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.OrderDto;
import com.devjoliveira.jocommerce.entities.Order;
import com.devjoliveira.jocommerce.entities.OrderItem;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.enums.OrderStatus;
import com.devjoliveira.jocommerce.utils.ProductFactory;
import com.devjoliveira.jocommerce.utils.TokenUtil;
import com.devjoliveira.jocommerce.utils.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenUtil tokenUtil;

  @Autowired
  private ObjectMapper objectMapper;

  private String adminToken, clientToken, invalidToken;

  private Long existingOrderId, nonExistingOrderId;

  private Order order;
  private Instant orderMoment;

  private User user;

  @BeforeEach
  void setUp() throws Exception {

    adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com",
        "123456");
    clientToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com",
        "123456");
    invalidToken = adminToken + "xpto"; // token - simulates wrong password

    existingOrderId = 1L;
    nonExistingOrderId = 999L;
    orderMoment = Instant.now();

    user = UserFactory.createUserClient();
    order = new Order(null, orderMoment, OrderStatus.WAITING_PAYMENT, user, null);

    Product product = ProductFactory.createProduct();

    OrderItem orderItem = new OrderItem(order, product, 2, 10.0);

    order.getItems().add(orderItem);
    order.getItems().add(orderItem);

  }

  @Test
  public void findById_ShouldReturnOrderDto_WhenIdExistsAndAdminLogged() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(1L));
    result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
    result.andExpect(jsonPath("$.status").value("PAID"));
    result.andExpect(jsonPath("$.userMin").exists());
    result.andExpect(jsonPath("$.userMin.name").value("Maria Brown"));
    result.andExpect(jsonPath("$.payment").exists());
    result.andExpect(jsonPath("$.items").exists());
    result.andExpect(jsonPath("$.items[0].productId").value(1L));
    result.andExpect(jsonPath("$.items[0].name").value("The Lord of the Rings"));
    result.andExpect(jsonPath("$.total").value(1431.0));

  }

  @Test
  public void findById_ShouldReturnOrderDto_WhenIdExistsAndClientLogged() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(1L));
    result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
    result.andExpect(jsonPath("$.status").value("PAID"));
    result.andExpect(jsonPath("$.userMin").exists());
    result.andExpect(jsonPath("$.userMin.name").value("Maria Brown"));
    result.andExpect(jsonPath("$.payment").exists());
    result.andExpect(jsonPath("$.items").exists());
    result.andExpect(jsonPath("$.items[0].productId").value(1L));
    result.andExpect(jsonPath("$.items[0].name").value("The Lord of the Rings"));
    result.andExpect(jsonPath("$.total").value(1431.0));

  }

  @Test
  public void findById_ShouldReturnForbiden_WhenOrderDoesNotBelongUser() throws Exception {

    Long otherUserOrder = 2L;

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", otherUserOrder)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isForbidden());

  }

  @Test
  public void findById_ShouldReturnNotFound_WhenAdminLoggedAndOrderDoesNotExists() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", nonExistingOrderId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  public void findById_ShouldReturnNotFound_WhenClientLoggedAndOrderDoesNotExists() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", nonExistingOrderId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  public void findById_ShouldReturnUnauthorized_WhenInvalidToken() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/orders/{id}", existingOrderId)
            .header("Authorization", "Bearer " + invalidToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnauthorized());

  }

  @Test
  public void insert_ShouldReturnOrderDto_WhenLoggedAndValidData() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(new OrderDto(order));

    ResultActions result = mockMvc
        .perform(post("/orders")
            .header("Authorization", "Bearer " + clientToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").value(4L)); // there are 3 categories in database
    result.andExpect(jsonPath("$.status").value("WAITING_PAYMENT"));
    result.andExpect(jsonPath("$.userMin.id").value(1L)); //

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenLoggedAndValidData() throws Exception {

    order.getItems().clear();

    String jsonBody = objectMapper.writeValueAsString(new OrderDto(order));

    ResultActions result = mockMvc
        .perform(post("/orders")
            .header("Authorization", "Bearer " + clientToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insertShouldReturnForbiddenWhenAdminLogged() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(new OrderDto(order));

    ResultActions result = mockMvc.perform(post("/orders")
        .header("Authorization", "Bearer " + adminToken)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isForbidden());
  }

  @Test
  public void insert_ShouldReturnUnauntorizes_WhenInvalidToken() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(new OrderDto(order));

    ResultActions result = mockMvc
        .perform(post("/orders")
            .header("Authorization", "Bearer " + invalidToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isUnauthorized());

  }

}
