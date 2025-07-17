package com.devjoliveira.jocommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjoliveira.jocommerce.dto.UserDto;
import com.devjoliveira.jocommerce.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserAuthenticated() {
    return ResponseEntity.ok().body(userService.getUserAuthenticated());
  }

}
