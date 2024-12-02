package org.example.taskbe.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @ApiResponse(
            responseCode = "200",
            description = "A list of tasks",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
    )
    @GetMapping("tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(toTaskDtoList(taskService.getAllTasks()));
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The task details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int id) {
        Optional<TaskEntity> taskEntityOptional;
        try {
            taskEntityOptional = taskService.getById(id);
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.notFound().build();
        }

        return taskEntityOptional.map(taskEntity -> ResponseEntity.ok(TaskDto.toDto(taskEntity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get tasks by title", description = "Retrieve tasks that match a specific title")
    @ApiResponse(
            responseCode = "200",
            description = "A list of tasks",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
    )
    @GetMapping("tasks/title")
    public ResponseEntity<List<TaskDto>> getTaskByTitle(@RequestParam String title) {
        return ResponseEntity.ok(toTaskDtoList(taskService.getTaskByTitle(title)));
    }

    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    @ApiResponse(
            responseCode = "201",
            description = "The created task",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
    )
    @PostMapping("tasks")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto toSave) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                TaskDto.toDto(
                        taskService.saveTask(toSave)));
    }

    @Operation(summary = "Edit a task", description = "Edit an existing task by its ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The updated task",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("tasks/{id}")
    public ResponseEntity<TaskDto> editTask(@Valid @RequestBody TaskDto toEdit, @PathVariable int id) {
        return ResponseEntity.ok(TaskDto.toDto(taskService.editTask(toEdit, id)));
    }

    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    private List<TaskDto> toTaskDtoList(List<TaskEntity> taskDtos) {
        List<TaskDto> taskEntities = new ArrayList<>();
        taskDtos.forEach(taskDto -> {
            taskEntities.add(TaskDto.toDto(taskDto));
        });
        return taskEntities;
    }
}
