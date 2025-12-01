package com.hamzabnr.ClientModelCrud.Repositories;

import com.hamzabnr.ClientModelCrud.Models.Task;
import com.hamzabnr.ClientModelCrud.Models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByStatus(TaskStatus status);

  List<Task> findByDueDateBefore(LocalDate date);

  @Query("select t from Task t where size(t.clients) > ?1")
  List<Task> findTasksWithMoreThanNClients(int n);
}
