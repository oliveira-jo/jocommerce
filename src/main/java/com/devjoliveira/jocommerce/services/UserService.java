package com.devjoliveira.jocommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devjoliveira.jocommerce.entities.Role;
import com.devjoliveira.jocommerce.entities.User;
import com.devjoliveira.jocommerce.projections.UserDetailsProjection;
import com.devjoliveira.jocommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

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

}
