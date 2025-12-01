package com.hamzabnr.ClientModelCrud.dto;

import com.hamzabnr.ClientModelCrud.Models.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Client Data Transfer Object")
public class ClientDTO {

  // @NotBlank(message = "Name must not be blank")
  @Size(min = 2, max = 50, message = "name must be between 2 and 50 caracters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  private List<Task> tasks;

  public ClientDTO() {
  }

  public ClientDTO(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public ClientDTO(String name, String email, List<Task> tasks) {
    this.name = name;
    this.email = email;
    this.tasks = tasks;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }
}
