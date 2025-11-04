package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Exceptions.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleClientNotFoundException(ClientNotFoundException exception) {
    Map<String, Object> body = Map.of(
        "timestamp", LocalDateTime.now(),
        "status", HttpStatus.NOT_FOUND.value(),
        "error", "Client Not Found",
        "message", exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}
