package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Services.TaskService;
import com.hamzabnr.ClientModelCrud.dto.TaskCreateDTO;
import com.hamzabnr.ClientModelCrud.dto.TaskDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/clients/{clientId}")
  public ResponseEntity<TaskDTO> createForClient(@PathVariable Long clientId, @Valid @RequestBody TaskCreateDTO create) {
    TaskDTO created = taskService.createTaskForClient(create, clientId);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping
  // will create a task without assigning to any client
  public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreateDTO create) {
    TaskDTO created  = taskService.createTaskForClient(create, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(created );
  }

  @PostMapping("/{taskId}/clients/{clientId}")
  // will assign a task to a client
  public ResponseEntity<TaskDTO> assignToClient(@PathVariable Long taskId, @PathVariable Long clientId) {
    TaskDTO assigned = taskService.assignClientToTask(taskId, clientId);
    return ResponseEntity.status(HttpStatus.CREATED).body(assigned);
  }

  @DeleteMapping("/{taskId}/assign/{clientId}")
  public ResponseEntity<Map<String,String>> unassign(@PathVariable Long taskId, @PathVariable Long clientId) {
    taskService.unassignTaskFromClient(taskId, clientId);
    return ResponseEntity.ok(Map.of("status", "unassigned"));
  }

  @GetMapping("/client/{clientId}")
  public ResponseEntity<List<TaskDTO>> getClientTasks(@PathVariable Long clientId) {
    return ResponseEntity.ok(taskService.getTasksByClientId(clientId));
  }

  @GetMapping
  public ResponseEntity<List<TaskDTO>> getAll() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskCreateDTO updates) {
    return ResponseEntity.ok(taskService.updateTask(id, updates));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}
