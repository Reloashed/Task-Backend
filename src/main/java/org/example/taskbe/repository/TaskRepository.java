package org.example.taskbe.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.taskbe.domain.entity.TaskEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class TaskRepository {
    private List<TaskEntity> tasks = new ArrayList<>();

    public List<TaskEntity> findAll() {
        return tasks;
    }

    public Optional<TaskEntity> findById(int id) throws IndexOutOfBoundsException {
        return Optional.of(tasks.get(id));
    }

    public TaskEntity create(TaskEntity taskEntity) {
        taskEntity.setId(tasks.size());
        tasks.add(taskEntity);
        return taskEntity;
    }

    public List<TaskEntity> findByTitle(String title) {
        return tasks.stream().filter(taskEntity -> taskEntity.getTitle().contains(title)).toList();
    }
}
