package org.example.taskbe.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.taskbe.domain.Priority;
import org.example.taskbe.domain.Topic;
import org.example.taskbe.domain.dto.TaskDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity representing a task in the database")
public class TaskEntity {

    @NotNull
    @Schema(description = "Unique identifier for the task", example = "1", required = true)
    private Integer id;

    @NotNull
    @Schema(description = "Topic related to the task", example = "ENGLISH", required = true)
    private Topic topic;

    @NotBlank
    @Schema(description = "Title of the task", example = "Write an Essay", required = true)
    private String title;

    @Schema(description = "Detailed description of the task", example = "Write a 1000-word essay on Shakespeare")
    private String description;

    @Schema(description = "Due date and time for the task", example = "2024-12-31T23:59:59")
    private LocalDateTime dueAt;

    @NotNull
    @Schema(description = "Completion status of the task", example = "true", required = true)
    private boolean done;

    @NotNull
    @Schema(description = "Creation timestamp of the task", example = "2024-01-01T12:00:00", required = true)
    private LocalDateTime createdAt;

    @NotNull
    @Schema(description = "Priority level of the task", example = "CRITICAL", required = true)
    private Priority priority;

    public static TaskEntity toEntity(TaskDto dto) {
        return new TaskEntity(
                dto.getId(),
                dto.getTopic(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getDueAt(),
                dto.isDone(),
                dto.getCreatedAt(),
                dto.getPriority()
        );
    }
}
