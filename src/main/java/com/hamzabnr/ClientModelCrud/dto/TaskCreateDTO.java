package com.hamzabnr.ClientModelCrud.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class TaskCreateDTO {
  @NotBlank
  private String title;
  private String description;
  private String status; // optional, e.g. "TODO"
  private LocalDate dueDate;

  public TaskCreateDTO() {}

  public TaskCreateDTO(String title, String description, String status, LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.dueDate = dueDate;
  }

  // getters / setters
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public LocalDate getDueDate() { return dueDate; }
  public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
