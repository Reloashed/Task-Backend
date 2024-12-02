package org.example.taskbe.api.v1;

import org.example.taskbe.domain.Priority;
import org.example.taskbe.domain.Topic;
import org.example.taskbe.domain.dto.TaskDto;
import org.example.taskbe.domain.entity.TaskEntity;
import org.example.taskbe.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskEntity createSampleTaskEntity() {
        return new TaskEntity(
                1,
                Topic.MATHEMATICS,
                "Sample Task",
                "A sample task description",
                LocalDateTime.now().plusDays(3),
                false,
                LocalDateTime.now(),
                Priority.CRITICAL
        );
    }

    private TaskDto createSampleTaskDto() {
        return new TaskDto(
                null,
                Topic.MATHEMATICS,
                "Sample Task",
                "A sample task description",
                LocalDateTime.now().plusDays(3),
                false,
                null,
                Priority.CRITICAL
        );
    }

    @Test
    void shouldReturnAllTasks() throws Exception {
        // Arrange
        List<TaskEntity> taskEntities = List.of(createSampleTaskEntity());
        Mockito.when(taskService.getAllTasks()).thenReturn(taskEntities);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        // Arrange
        Mockito.when(taskService.getById(eq(1))).thenReturn(Optional.of(createSampleTaskEntity()));

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    void shouldReturnNotFoundForNonexistentTaskById() throws Exception {
        // Arrange
        Mockito.when(taskService.getById(eq(999))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnTasksByTitle() throws Exception {
        // Arrange
        List<TaskEntity> taskEntities = List.of(createSampleTaskEntity());
        Mockito.when(taskService.getTaskByTitle(eq("Sample Task"))).thenReturn(taskEntities);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/title")
                        .param("title", "Sample Task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        // Arrange
        Mockito.when(taskService.saveTask(any(TaskDto.class))).thenReturn(createSampleTaskEntity());

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"topic\": \"MATHEMATICS\",\n" +
                                "  \"title\": \"Sample Task\",\n" +
                                "  \"description\": \"A sample task description\",\n" +
                                "  \"dueAt\": \"2024-12-01T12:00:00\",\n" +
                                "  \"done\": false,\n" +
                                "  \"priority\": \"CRITICAL\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTaskWithInvalidData() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"dueAt\": \"2024-12-01T12:00:00\",\n" +
                                "  \"done\": false\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateTaskSuccessfully() throws Exception {
        // Arrange
        Mockito.when(taskService.editTask(any(TaskDto.class), eq(1))).thenReturn(createSampleTaskEntity());

        // Act & Assert
        mockMvc.perform(put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"topic\": \"MATHEMATICS\",\n" +
                                "  \"title\": \"Updated Task\",\n" +
                                "  \"description\": \"Updated description\",\n" +
                                "  \"dueAt\": \"2024-12-05T12:00:00\",\n" +
                                "  \"done\": false,\n" +
                                "  \"priority\": \"MAJOR\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Task")); // Verify the mock response
    }
}