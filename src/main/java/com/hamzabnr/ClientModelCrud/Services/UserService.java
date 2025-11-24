package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Models.UserModel;
import com.hamzabnr.ClientModelCrud.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserModel login(String username, String password) {
    UserModel user = userRepository.findByUsername(username)
        .filter(u -> encoder.matches(password, u.getPassword())).orElse(null);
    return user;
  }
}
