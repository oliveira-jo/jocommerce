package com.devjoliveira.jocommerce.projections;

public interface UserDetailsProjection {

  String getUsername();

  String getPassword();

  Long getRoleId();

  String getAuthority();

}
