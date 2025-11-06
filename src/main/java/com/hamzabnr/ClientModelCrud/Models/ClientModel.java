package com.hamzabnr.ClientModelCrud.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class ClientModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;

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

  @Override
  public String toString() {
    return "ClientModel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
