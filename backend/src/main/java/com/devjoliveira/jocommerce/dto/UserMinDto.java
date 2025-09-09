package com.devjoliveira.jocommerce.dto;

import com.devjoliveira.jocommerce.entities.User;

public record UserMinDto(

    Long id,
    String name) {

  public UserMinDto(User user) {
    this(
        user.getId(),
        user.getName());
  }

}
