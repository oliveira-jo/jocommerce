package com.devjoliveira.jocommerce.dto;

import java.time.LocalDate;
import java.util.List;

import com.devjoliveira.jocommerce.entities.Role;
import com.devjoliveira.jocommerce.entities.User;

public record UserDto(

    Long id,
    String name,
    String email,
    LocalDate birthDate,
    String phone,
    String password,
    List<String> roles

) {

  public UserDto(User user) {
    this(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getBirthDate(),
        user.getPhone(),
        user.getPassword(),
        user.getRoles().stream().map(Role::getAuthority).toList());
  }

}
