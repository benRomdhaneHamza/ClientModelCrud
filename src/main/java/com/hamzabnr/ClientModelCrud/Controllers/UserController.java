package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Models.UserModel;
import com.hamzabnr.ClientModelCrud.Repositories.UserRepository;
import com.hamzabnr.ClientModelCrud.Security.JwtUtil;
import com.hamzabnr.ClientModelCrud.Services.UserService;
import com.hamzabnr.ClientModelCrud.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest loginRequest) {
    System.out.println("loginRequest ........." + loginRequest);
    UserModel user = this.userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("Invalid username or password"));
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid username or password");
    }
    return jwtUtil.generateToken(user.getUsername(), user.getRole());
  }

}
