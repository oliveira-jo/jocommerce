package com.devjoliveira.jocommerce.controllersIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.utils.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenUtil tokenUtil;

  private String adminToken, clientToken, invalidToken;

  @BeforeEach
  void setUp() throws Exception {

    adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com",
        "123456");
    clientToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com",
        "123456");
    invalidToken = adminToken + "xpto"; // token - simulates wrong password

  }

  @Test
  public void getUserAuthenticated_ShouldReturnUserDto_WhenAdminLogged() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/users/me")
            .header("Authorization", "Bearer " + adminToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(2L));
    result.andExpect(jsonPath("$.name").value("Alex Green"));
    result.andExpect(jsonPath("$.email").value("alex@gmail.com"));
    result.andExpect(jsonPath("$.birthDate").value("1987-12-13"));
    result.andExpect(jsonPath("$.phone").value("977777777"));
    result.andExpect(jsonPath("$.roles[0]").value("ROLE_CLIENT"));
    result.andExpect(jsonPath("$.roles[1]").value("ROLE_ADMIN"));

  }

  @Test
  public void getUserAuthenticated_ShouldReturnUserDto_WhenClientLogged() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/users/me")
            .header("Authorization", "Bearer " + clientToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(1L));
    result.andExpect(jsonPath("$.name").value("Maria Brown"));
    result.andExpect(jsonPath("$.email").value("maria@gmail.com"));
    result.andExpect(jsonPath("$.birthDate").value("2001-07-25"));
    result.andExpect(jsonPath("$.phone").value("988888888"));
    result.andExpect(jsonPath("$.roles[0]").value("ROLE_CLIENT"));

  }

  @Test
  public void getUserAuthenticated_ShouldUnaunthorized_WhenInvalidToken() throws Exception {

    ResultActions result = mockMvc
        .perform(get("/users/me")
            .header("Authorization", "Bearer " + invalidToken)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()); // Debug

    result.andExpect(status().isUnauthorized());

  }

}
