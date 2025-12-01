package com.hamzabnr.ClientModelCrud.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class ClientModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;

  // owning side of the ManyToMany relationship
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "client_tasks",
      joinColumns = @JoinColumn(name = "client_id"),
      inverseJoinColumns = @JoinColumn(name = "task_id")
  )
  private Set<Task> tasks = new HashSet<>();

  public ClientModel() {
    // JPA requires a default constructor
  }

  public ClientModel(Long id, String email, String name) {
    this.id = id;
    this.email = email;
    this.name = name;
  }


  public ClientModel(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Set<Task> getTasks() {
    return tasks;
  }

  public void setTasks(Set<Task> tasks) {
    this.tasks = tasks;
  }

  // helpers
  public void addTask(Task task) {
    tasks.add(task);
    task.getClients().add(this);
  }

  public void removeTask(Task task) {
    tasks.remove(task);
    task.getClients().remove(this);
  }

  @Override
  public String toString() {
    return "ClientModel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
