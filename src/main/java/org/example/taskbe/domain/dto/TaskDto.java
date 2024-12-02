package org.example.taskbe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.taskbe.domain.Priority;
import org.example.taskbe.domain.Topic;
import org.example.taskbe.domain.entity.TaskEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Task")
public class TaskDto {

    @Schema(description = "Unique identifier for the task", example = "1")
    private Integer id;

    @NotNull
    @Schema(description = "Topic related to the task", example = "MATHEMATICS", required = true)
    private Topic topic;

    @NotBlank
    @Schema(description = "Title of the task", example = "Complete Algebra Assignment", required = true)
    private String title;

    @Schema(description = "Detailed description of the task", example = "Complete all exercises from chapter 3")
    private String description;

    @Schema(description = "Due date and time for the task", example = "2024-12-31T23:59:59")
    private LocalDateTime dueAt;

    @NotNull
    @Schema(description = "Completion status of the task", example = "false", required = true)
    private boolean done;

    @Schema(description = "Creation timestamp of the task", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;

    @NotNull
    @Schema(description = "Priority level of the task", example = "MAJOR", required = true)
    private Priority priority;

    public static TaskDto toDto(TaskEntity entity) {
        return new TaskDto(
                entity.getId(),
                entity.getTopic(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueAt(),
                entity.isDone(),
                entity.getCreatedAt(),
                entity.getPriority()
        );
    }
}
