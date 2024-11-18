package org.example.taskbe.domain.entity;

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
public class TaskEntity {
    @NotNull
    private Integer id;
    @NotNull
    private Topic topic;
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime dueAt;
    @NotNull
    private boolean done;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
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
