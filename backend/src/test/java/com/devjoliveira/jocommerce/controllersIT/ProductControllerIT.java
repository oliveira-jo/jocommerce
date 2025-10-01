package com.devjoliveira.jocommerce.controllersIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.ProductDto;
import com.devjoliveira.jocommerce.entities.Category;
import com.devjoliveira.jocommerce.entities.Product;
import com.devjoliveira.jocommerce.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenUtil tokenUtil;

  @Autowired
  private ObjectMapper objectMapper;

  private String adminToken, clientToken, invalidToken;
  private Long existingProductId, nonExistingProductId, dependentProductId;
  private String productName;
  private Product product;
  private ProductDto productDto;

  @BeforeEach
  void setUp() throws Exception {

    productName = "Macbook";

    adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com",
        "123456");
    clientToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com",
        "123456");
    invalidToken = adminToken + "xpto"; // token - simulates wrong password

    existingProductId = 2L;
    nonExistingProductId = 999L;
    dependentProductId = 3L;

    Category cat = new Category(2L, "Eletro");
    product = new Product(null, "Meu produto", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. ", 999.90,
        "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
    product.getCategories().add(cat);

    productDto = new ProductDto(product);

  }

  @Test
  public void findAll_ShouldReturnPage_WhenNameParamIsNotEmpty() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/products?name={productName}", productName).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.content[0].id").value(3L));
    result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    result.andExpect(jsonPath("$.content[0].price").value(1250.0));
    result.andExpect(jsonPath("$.content[0].imageUrl")
        .value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));

  }

  @Test
  public void findAll_ShouldReturnPage_WhenNameIsEmpty() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/products", productName).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.content[0].id").value(1L));
    result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
    result.andExpect(jsonPath("$.content[0].price").value(90.5));
    result.andExpect(jsonPath("$.content[0].imageUrl")
        .value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));

  }

  @Test
  public void findById_ShouldReturnProductDto_WhenIdExist() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/products/{id}", existingProductId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(2L));
    result.andExpect(jsonPath("$.name").value("Smart TV"));
    result.andExpect(jsonPath("$.imageUrl").value(
        "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"));
    result.andExpect(jsonPath("$.price").value(2190.0));
    result.andExpect(jsonPath("$.categories").exists());
    result.andExpect(jsonPath("$.categories[0].id").value(2L));
    result.andExpect(jsonPath("$.categories[0].name").value("Eletrônicos"));

  }

  @Test
  public void findById_ShouldReturnNotFound_WhenNonExistId() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/products/{id}", nonExistingProductId).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  public void insert_ShouldReturnProductDtoCreated_WhenAdminLoggedAndValidData() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(productDto);

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").value(26L));
    result.andExpect(jsonPath("$.name").value(product.getName()));
    result.andExpect(jsonPath("$.description").value(product.getDescription()));
    result.andExpect(jsonPath("$.price").value(product.getPrice()));
    result.andExpect(jsonPath("$.imageUrl").value(product.getImageUrl()));
    result.andExpect(jsonPath("$.categories[0].id").value(2L));

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenAdminLoggedAndInvalidName() throws Exception {

    product.setName("P");

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenAdminLoggedAndInvalidDescription() throws Exception {

    product.setDescription("D");

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenAdminLoggedAndPriceIsNegative() throws Exception {

    product.setPrice(-10.0);

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenAdminLoggedAndPriceIsZero() throws Exception {

    product.setPrice(0.0);

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insert_ShouldReturnUnprocessableEntity_WhenAdminLoggedAndCategoryIsEmpty() throws Exception {

    product.getCategories().clear();

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + adminToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnprocessableEntity());

  }

  @Test
  public void insert_ShouldReturnForbidden_WhenClientLogged() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + clientToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isForbidden());

  }

  @Test
  public void insert_ShouldReturnUnauthorized_WhenTokenIsInvalid() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(new ProductDto(product));

    ResultActions result = mockMvc
        .perform(post("/products")
            .header("Authorization", "Bearer " + invalidToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnauthorized());

  }

  @Test
  public void delete_ShouldDeleteProduct_WhenAdminLoggedAndProductIdExists() throws Exception {

    ResultActions result = mockMvc
        .perform(delete("/products/{id}", existingProductId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNoContent());

  }

  @Test
  public void delete_ShouldReturnNotFound_WhenAdminLoggedAndProductIdIsNotExist() throws Exception {

    ResultActions result = mockMvc
        .perform(delete("/products/{id}", nonExistingProductId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  @Transactional(propagation = Propagation.SUPPORTS) // For use with dependent ids
  public void delete_ShouldReturnBadRequest_WhenAdminLoggedAndProductIdIsDependent() throws Exception {

    ResultActions result = mockMvc
        .perform(delete("/products/{id}", dependentProductId)
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isBadRequest());

  }

  @Test
  public void delete_ShouldReturnForbidden_WhenClientLogged() throws Exception {

    ResultActions result = mockMvc
        .perform(delete("/products/{id}", existingProductId)
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isForbidden());

  }

  @Test
  public void delete_ShouldReturnUnauthorized_WhenTokenIsInvalid() throws Exception {

    ResultActions result = mockMvc
        .perform(delete("/products/{id}", existingProductId)
            .header("Authorization", "Bearer " + invalidToken)
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isUnauthorized());

  }

}
