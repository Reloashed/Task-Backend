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
import org.springframework.test.web.servlet.ResultActions;

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

    private TaskEntity getSampleTaskEntity() {
        return new TaskEntity(
                1,
                Topic.MATHEMATICS,
                "Task 1",
                "Description 1",
                LocalDateTime.now().plusDays(1),
                false,
                LocalDateTime.now(),
                Priority.CRITICAL
        );
    }

    private TaskDto getSampleTaskDto() {
        return new TaskDto(
                null,
                Topic.MATHEMATICS,
                "Task 1",
                "Description 1",
                LocalDateTime.now().plusDays(1),
                false,
                null,
                Priority.CRITICAL
        );
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<TaskEntity> taskEntities = Arrays.asList(getSampleTaskEntity());
        Mockito.when(taskService.getAllTasks()).thenReturn(taskEntities);

        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void testGetTaskById() throws Exception {
        Mockito.when(taskService.getById(eq(1))).thenReturn(Optional.of(getSampleTaskEntity()));

        mockMvc.perform(get("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    void testGetTaskById_NotFound() throws Exception {
        Mockito.when(taskService.getById(eq(999))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTaskByTitle() throws Exception {
        List<TaskEntity> taskEntities = Arrays.asList(getSampleTaskEntity());
        Mockito.when(taskService.getTaskByTitle(eq("Task 1"))).thenReturn(taskEntities);

        mockMvc.perform(get("/api/v1/tasks/title")
                        .param("title", "Task 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void testCreateTask() throws Exception {
        Mockito.when(taskService.saveTask(any(TaskDto.class))).thenReturn(getSampleTaskEntity());

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": null,\n" +
                                "  \"topic\": \"MATHEMATICS\",\n" +
                                "  \"title\": \"Task 1\",\n" +
                                "  \"description\": \"Description 1\",\n" +
                                "  \"dueAt\": \"2024-12-01T12:00:00\",\n" +
                                "  \"done\": false,\n" +
                                "  \"createdAt\": null,\n" +
                                "  \"priority\": \"CRITICAL\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    void testCreateTask_returnsBadRequest() throws Exception {
        Mockito.when(taskService.saveTask(any(TaskDto.class))).thenReturn(getSampleTaskEntity());

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": null,\n" +
                                "  \"dueAt\": \"2024-12-01T12:00:00\",\n" +
                                "  \"done\": false,\n" +
                                "  \"createdAt\": null,\n" +
                                "  \"priority\": \"CRITICAL\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}