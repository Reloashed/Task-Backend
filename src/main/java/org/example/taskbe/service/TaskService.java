package org.example.taskbe.service;

import org.example.taskbe.domain.dto.TaskDto;
import org.example.taskbe.domain.entity.TaskEntity;
import org.example.taskbe.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<TaskEntity> getById(int id) throws IndexOutOfBoundsException {
        return taskRepository.findById(id);
    }

    public TaskEntity saveTask(TaskDto taskDto) {
        taskDto.setCreatedAt(LocalDateTime.now());
        return taskRepository.create(TaskEntity.toEntity(taskDto));
    }

    public List<TaskEntity> getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public TaskEntity editTask(TaskDto taskDto, int id) {
        taskDto.setId(id);
        taskDto.setCreatedAt(LocalDateTime.now());
        return taskRepository.create(TaskEntity.toEntity(taskDto));
    }

    public void deleteTask(int id) {
        taskRepository.delete(id);
    }
}
