package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Config.RequestResponseLogger;
import com.hamzabnr.ClientModelCrud.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogger.class);
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(u -> {
          return User.withUsername(u.getUsername())
              .password(u.getPassword())      // already BCrypt-hashed in DB
              .roles(u.getRole())            // store role as "ADMIN" or "USER" in DB
              .build();
        })
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }
}
