package com.devjoliveira.jocommerce.utils;

import java.time.LocalDate;

import com.devjoliveira.jocommerce.entities.Role;
import com.devjoliveira.jocommerce.entities.User;

public class UserFactory {

  public static User createUserClient() {

    User user = new User(1L,
        "Maria Brown",
        "maria@gmail.com",
        "988888888",
        LocalDate.of(2001, 07, 25),
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy");

    user.addRole(new Role(1L, "ROLE_CLIENT"));

    return user;
  }

  public static User createUserAdmin() {

    User user = new User(1L,
        "Alex Green",
        "alex@gmail.com",
        "977777777",
        LocalDate.of(1987, 12, 13),
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy");

    user.addRole(new Role(1L, "ROLE_CLIENT"));
    user.addRole(new Role(2L, "ROLE_ADMIN"));

    return user;
  }

  public static User createCustonUserClient(Long id, String name, String email) {

    User user = new User(id,
        name,
        email,
        "988888888",
        LocalDate.of(2001, 07, 25),
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy");

    user.addRole(new Role(1L, "ROLE_CLIENT"));

    return user;
  }

  public static User createCustonUserAdmin(Long id, String name, String email) {

    User user = new User(id,
        name,
        email,
        "977777777",
        LocalDate.of(1987, 12, 13),
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy");

    user.addRole(new Role(1L, "ROLE_CLIENT"));
    user.addRole(new Role(2L, "ROLE_ADMIN"));

    return user;
  }

}
