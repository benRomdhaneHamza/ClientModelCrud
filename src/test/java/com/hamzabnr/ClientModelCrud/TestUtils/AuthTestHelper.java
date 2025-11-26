package com.hamzabnr.ClientModelCrud.TestUtils;

import com.hamzabnr.ClientModelCrud.Models.UserModel;
import com.hamzabnr.ClientModelCrud.Repositories.UserRepository;
import com.hamzabnr.ClientModelCrud.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthTestHelper {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthTestHelper(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public String getAdminToken() {
    UserModel user = userRepository.findByUsername("admin").orElseGet(() -> {
      UserModel newUser = new UserModel();
      newUser.setUsername("admin");
      newUser.setPassword(passwordEncoder.encode("admin"));
      newUser.setRole("ADMIN");
      return userRepository.save(newUser);
    });
    return jwtUtil.generateToken(user.getUsername(), user.getRole());
  }

}
