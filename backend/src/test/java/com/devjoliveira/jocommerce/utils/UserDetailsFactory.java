package com.devjoliveira.jocommerce.utils;

import java.util.ArrayList;
import java.util.List;

import com.devjoliveira.jocommerce.projections.UserDetailsProjection;

public class UserDetailsFactory {

  public static List<UserDetailsProjection> createCustomClientUser(String email) {
    List<UserDetailsProjection> list = new ArrayList<>();
    list.add(new UserDetailsProjectionImpl(1L, email,
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy", "ROLE_CLIENT"));
    return list;
  }

  public static List<UserDetailsProjection> createCustomAdminUser(String email) {
    List<UserDetailsProjection> list = new ArrayList<>();
    list.add(new UserDetailsProjectionImpl(2L, email,
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy", "ROLE_ADMIN"));
    return list;
  }

  public static List<UserDetailsProjection> createCustomAdminClientUser(String email) {
    List<UserDetailsProjection> list = new ArrayList<>();
    list.add(new UserDetailsProjectionImpl(1L, email,
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy", "ROLE_CLIENT"));
    list.add(new UserDetailsProjectionImpl(2L, email,
        "$2a$10$Bhcw4tAiXmtMtrHOCNYzEeRoNCI4rgbgQ/OrP3RYb43Y71CZ7K0Hy", "ROLE_ADMIN"));
    return list;
  }

}

class UserDetailsProjectionImpl implements UserDetailsProjection {

  private String username;
  private String password;
  private Long roleId;
  private String authority;

  public UserDetailsProjectionImpl() {
  }

  public UserDetailsProjectionImpl(Long roleId, String username, String password, String authority) {
    this.roleId = roleId;
    this.username = username;
    this.password = password;
    this.authority = authority;
  }

  @Override
  public Long getRoleId() {
    return roleId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

}
