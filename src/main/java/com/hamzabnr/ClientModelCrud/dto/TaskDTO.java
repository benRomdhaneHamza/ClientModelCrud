package com.hamzabnr.ClientModelCrud.dto;

import java.time.LocalDate;

public class TaskDTO {
  private Long id;
  private String title;
  private String description;
  private String status; // TaskStatus.name()
  private LocalDate dueDate;

  public TaskDTO() {}

  public TaskDTO(Long id, String title, String description, String status, LocalDate dueDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.dueDate = dueDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }
}
