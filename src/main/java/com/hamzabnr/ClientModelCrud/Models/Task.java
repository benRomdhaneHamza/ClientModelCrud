package com.hamzabnr.ClientModelCrud.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status = TaskStatus.TODO;

  private LocalDate dueDate;

  @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY)
  private Set<ClientModel> clients = new HashSet<>();

  public Task() {}

  public Task(String title, String description, TaskStatus status, LocalDate dueDate, Set<ClientModel> clients) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.dueDate = dueDate;
    this.clients = clients;
  }

  public Task(String title, String description, TaskStatus status, LocalDate dueDate) {
    this.dueDate = dueDate;
    this.status = status;
    this.description = description;
    this.title = title;
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

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Set<ClientModel> getClients() {
    return clients;
  }

  public void setClients(Set<ClientModel> clients) {
    this.clients = clients;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // helpers
  public void addClient(ClientModel client) {
    this.clients.add(client);
    client.getTasks().add(this);
  }

  public void removeClient(ClientModel client) {
    this.clients.remove(client);
    client.getTasks().remove(this);
  }

  @Override
  public String toString() {
    return "Task{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", status=" + status +
        ", dueDate=" + dueDate +
        ", clients=" + clients +
        '}';
  }
}
