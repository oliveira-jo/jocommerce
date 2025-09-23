package com.devjoliveira.jocommerce.services;

import org.springframework.stereotype.Service;

import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

  private final UserService userService;

  public AuthService(UserService userService) {
    this.userService = userService;
  }

  public void validateSelfOrAdmin(Long userId) {
    User user = userService.authenticated();
    if (!user.hasRole("ROLE_ADMIN") && !user.getId().equals(userId)) {
      throw new ForbiddenException("Access denied! You do not have permission to perform this action");
    }
  }

}
