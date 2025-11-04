package com.hamzabnr.ClientModelCrud.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HealthCheck {

  @GetMapping("/health")
  public String healthCheck() {
    return "Api is running ...";
  }
}
