package org.example.taskbe.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.taskbe.domain.entity.TaskEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (taskEntity.getId() == null || tasks.stream().filter(task -> task.getId().intValue() == taskEntity.getId().intValue()).toList().isEmpty()) {
            taskEntity.setId(tasks.size());
            tasks.add(taskEntity);
        } else {
            tasks.remove(taskEntity.getId().intValue());
            tasks.add(taskEntity.getId(), taskEntity);
        }

        return taskEntity;
    }

    public List<TaskEntity> findByTitle(String title) {
        return tasks.stream().filter(taskEntity -> taskEntity.getTitle().contains(title)).toList();
    }

    public void delete(int id) {
        tasks.remove(id);
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setId(i);
        }
    }
}
