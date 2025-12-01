package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Models.Task;
import com.hamzabnr.ClientModelCrud.Models.TaskStatus;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import com.hamzabnr.ClientModelCrud.Repositories.TaskRepository;
import com.hamzabnr.ClientModelCrud.dto.TaskCreateDTO;
import com.hamzabnr.ClientModelCrud.dto.TaskDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final ClientRepository clientRepository;

  public TaskService(TaskRepository taskRepository, ClientRepository clientRepository) {
    this.taskRepository = taskRepository;
    this.clientRepository = clientRepository;
  }

  // helper to map entity -> dto
  private TaskDTO toDTO(Task t) {
    return new TaskDTO(
        t.getId(),
        t.getTitle(),
        t.getDescription(),
        t.getStatus() != null ? t.getStatus().name() : null,
        t.getDueDate()
    );
  }

  // create a task and assign to a client (if clientId provided)
  @Transactional
  public TaskDTO createTaskForClient(TaskCreateDTO create, Long clientId) {
    TaskStatus status = TaskStatus.TODO;

    if (create.getStatus() != null) {
      try {
        status = TaskStatus.valueOf(create.getStatus());
      } catch (IllegalArgumentException e) {
        // keep TODO or throw if you prefer
        status = TaskStatus.TODO;
      }
    }

    Task task = new Task(create.getTitle(), create.getDescription(), status, create.getDueDate());
    Task savedTask = taskRepository.save(task);

    if (clientId != null) {
      ClientModel client = clientRepository.findById(clientId)
          .orElseThrow(() -> new NoSuchElementException("Client not found: " + clientId));
      client.addTask(savedTask);
      clientRepository.save(client);
    }
    return toDTO(savedTask);
  }

  // assign existing task to existing client
  @Transactional
  public TaskDTO assignClientToTask(Long taskId, Long clientId) {
    Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    ClientModel client = clientRepository.findById(clientId)
        .orElseThrow(() -> new NoSuchElementException("Client not found: " + clientId));

    client.addTask(existingTask);
    clientRepository.save(client);

    return toDTO(existingTask);
  }

  @Transactional
  public void unassignTaskFromClient(Long taskId, Long clientId) {
    Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    ClientModel client = clientRepository.findById(clientId)
        .orElseThrow(() -> new NoSuchElementException("Client not found: " + clientId));
    client.removeTask(existingTask);
    clientRepository.save(client);
  }

  // list tasks for a client
  @Transactional(readOnly = true)
  public List<TaskDTO> getTasksByClientId(Long clientId) {
    ClientModel client = clientRepository.findById(clientId)
        .orElseThrow(() -> new NoSuchElementException("Client not found: " + clientId));
    // convert each Task to DTO
    return client.getTasks().stream().map(this::toDTO).collect(Collectors.toList());
  }

  // get all tasks
  @Transactional(readOnly = true)
  public List<TaskDTO> getAllTasks() {
    return taskRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
  }

  // update task
  @Transactional
  public TaskDTO updateTask(Long taskId, TaskCreateDTO updates) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    if (updates.getTitle() != null) task.setTitle(updates.getTitle());
    if (updates.getDescription() != null) task.setDescription(updates.getDescription());
    if (updates.getStatus() != null) {
      try {
        task.setStatus(TaskStatus.valueOf(updates.getStatus()));
      } catch (IllegalArgumentException ignored) {}
    }
    if (updates.getDueDate() != null) task.setDueDate(updates.getDueDate());

    Task saved = taskRepository.save(task);
    return toDTO(saved);
  }

  // delete task
  @Transactional
  public void deleteTask(Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    // remove associations from clients first to keep DB consistent
    task.getClients().forEach(client -> client.getTasks().remove(task));
    taskRepository.delete(task);
  }

}
