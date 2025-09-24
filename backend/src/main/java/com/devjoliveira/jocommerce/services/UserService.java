package com.devjoliveira.jocommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocommerce.dto.UserDto;
import com.devjoliveira.jocommerce.entities.Role;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.projections.UserDetailsProjection;
import com.devjoliveira.jocommerce.repositories.UserRepository;
import com.devjoliveira.jocommerce.utils.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final CustomUserUtil customUserUtil;

  public UserService(UserRepository userRepository, CustomUserUtil customUserUtil) {
    this.userRepository = userRepository;
    this.customUserUtil = customUserUtil;
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
      String username = customUserUtil.getLoggedUsername();
      return userRepository.findByEmail(username).get();

    } catch (Exception e) {
      throw new UsernameNotFoundException("User not found: ");

    }

  }

}
