package com.devjoliveira.jocommerce.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.projections.UserDetailsProjection;
import com.devjoliveira.jocommerce.repositories.UserRepository;
import com.devjoliveira.jocommerce.utils.CustomUserUtil;
import com.devjoliveira.jocommerce.utils.UserDetailsFactory;
import com.devjoliveira.jocommerce.utils.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

  @InjectMocks
  private UserService service;

  @Mock
  private UserRepository repository;

  @Mock
  private CustomUserUtil userUtil;

  private String existingEmail;
  private String nonExistingEmail;

  private User user;
  private List<UserDetailsProjection> userdetailsProjectionList;

  @BeforeEach
  void setUp() throws Exception {

    existingEmail = "maria@gmail.com";
    nonExistingEmail = "user@gmail.com";

    user = UserFactory.createCustonUserClient(1L, existingEmail);
    userdetailsProjectionList = UserDetailsFactory.createCustomAdminUser(existingEmail);

    Mockito.when(repository.searchUserAndRolesByEmail(existingEmail)).thenReturn(userdetailsProjectionList);
    Mockito.when(repository.searchUserAndRolesByEmail(nonExistingEmail)).thenReturn(List.of());

    Mockito.when(repository.findByEmail(existingEmail)).thenReturn(Optional.of(user));
    Mockito.when(repository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

  }

  @Test
  public void loadUserByUsername_ShouldReturnUserDetails_WhenEmailExists() {

    UserDetails result = service.loadUserByUsername(existingEmail);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getUsername(), existingEmail);
    Assertions.assertEquals(result.getPassword(), user.getPassword());

  }

  @Test
  public void loadUserByUsername_ShouldThrowUsernameNotFound_WhenUserDoesNotExists() {

    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      service.loadUserByUsername(nonExistingEmail);
    });

  }

  @Test
  public void getUserAuthenticated_ShouldReturnUserDto_WhenEmailIsValid() {

    Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingEmail);
    UserDetails result = service.loadUserByUsername(existingEmail);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getUsername(), existingEmail);
    Assertions.assertEquals(result.getPassword(), user.getPassword());

  }

  @Test
  public void getUserAuthenticated_ShouldThrowUsernameNotFound_WhenEmailDoesNotExists() {

    Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      service.loadUserByUsername(nonExistingEmail);
    });

  }

  @Test
  public void authenticated_ShouldReturnUser_WhenEmailIsValid() {

    Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingEmail);
    User result = service.authenticated();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), 1L);
    Assertions.assertEquals(result.getEmail(), existingEmail);

  }

  @Test
  public void authenticated_ShouldThrowUsernameNotFound_WhenEmailDoesNotExists() {

    Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      service.authenticated();
    });

  }

}
