package org.example.taskbe.api.v1;

import jakarta.validation.Valid;
import org.example.taskbe.domain.dto.TaskDto;
import org.example.taskbe.domain.entity.TaskEntity;
import org.example.taskbe.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(toTaskDtoList(taskService.getAllTasks()));
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int id) {
        Optional<TaskEntity> taskEntityOptional;
        try {
            taskEntityOptional = taskService.getById(id);
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.notFound().build();
        }

        return taskEntityOptional.map(taskEntity -> ResponseEntity.ok(TaskDto.toDto(taskEntity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("tasks/title")
    public ResponseEntity<List<TaskDto>> getTaskByTitle(@RequestParam String title) {
        return ResponseEntity.ok(toTaskDtoList(taskService.getTaskByTitle(title)));
    }

    @PostMapping("tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto toSave) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                TaskDto.toDto(
                        taskService.saveTask(toSave)));
    }

    private List<TaskDto> toTaskDtoList(List<TaskEntity> taskDtos) {
        List<TaskDto> taskEntities = new ArrayList<>();
        taskDtos.forEach(taskDto -> {
            taskEntities.add(TaskDto.toDto(taskDto));
        });
        return taskEntities;
    }
}
