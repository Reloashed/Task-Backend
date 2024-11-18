package org.example.taskbe.domain.dto;

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
public class TaskDto {
    private Integer id;
    @NotNull
    private Topic topic;
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime dueAt;
    @NotNull
    private boolean done;
    private LocalDateTime createdAt;
    @NotNull
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
