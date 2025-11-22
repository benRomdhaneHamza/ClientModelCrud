package com.hamzabnr.ClientModelCrud.Exceptions;

public class AccessUnauthorizedException extends Exception {
  public AccessUnauthorizedException() {
    super("Access to this resource is unauthorized");
  }
}
