package com.devjoliveira.jocommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.services.exceptions.ForbiddenException;
import com.devjoliveira.jocommerce.utils.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

  @InjectMocks
  private AuthService authService;

  @Mock
  private UserService userService;

  private User admin, selfClient, otherClient;

  @BeforeEach
  void setUp() throws Exception {
    admin = UserFactory.createUserAdmin();
    selfClient = UserFactory.createCustonUserClient(2L, "Bob", "bob@gmail.com");
    otherClient = UserFactory.createCustonUserClient(3L, "Ana", "ana@gmail.com");

  }

  @Test
  public void validateSelfOrAdmin_ShouldDoNothing_WhenUserIsAdmin() {

    Mockito.when(userService.authenticated()).thenReturn(admin);

    Assertions.assertDoesNotThrow(() -> {
      authService.validateSelfOrAdmin(admin.getId());
    });

  }

  @Test
  public void validateSelfOrAdmin_ShouldDoNothing_WhenUserIsHimself() {

    Mockito.when(userService.authenticated()).thenReturn(selfClient);

    Assertions.assertDoesNotThrow(() -> {
      authService.validateSelfOrAdmin(selfClient.getId());
    });

  }

  @Test
  public void validateSelfOrAdmin_ShouldThrowForbiddenException_WhenUserIsNotHimselfOrAdmin() {

    Mockito.when(userService.authenticated()).thenReturn(selfClient);

    Assertions.assertThrows(ForbiddenException.class, () -> {
      authService.validateSelfOrAdmin(otherClient.getId());
    });

  }

}
