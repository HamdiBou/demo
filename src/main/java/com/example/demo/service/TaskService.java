package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Task;

public interface TaskService {
    List<Task> findAllByUserId(String userId);
    Optional<Task> findById(String id);
    Task save(Task task);
    Task update(String id, Task task);
    void delete(String id);
}
