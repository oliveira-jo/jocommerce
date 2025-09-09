package com.devjoliveira.jocommerce.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.UserDto;
import com.devjoliveira.jocommerce.entities.Role;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.projections.UserDetailsProjection;
import com.devjoliveira.jocommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {

    List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(email);

    if (result.isEmpty()) {
      throw new UsernameNotFoundException("Email not found: " + email);
    }

    User user = new User();
    user.setEmail(result.get(0).getUsername());
    user.setPassword(result.get(0).getPassword());

    for (UserDetailsProjection projection : result) {
      user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
    }

    return user;

  }

  @Transactional(readOnly = true)
  public UserDto getUserAuthenticated() {
    User user = authenticated();
    return new UserDto(user);
  }

  protected User authenticated() {

    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
      String username = jwtPrincipal.getClaim("username");

      return userRepository.findByEmail(username).get();
    } catch (Exception e) {
      throw new UsernameNotFoundException("User not found: ");
    }

  }

}
