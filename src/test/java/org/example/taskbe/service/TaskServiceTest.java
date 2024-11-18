package org.example.taskbe.service;

import org.example.taskbe.domain.Priority;
import org.example.taskbe.domain.Topic;
import org.example.taskbe.domain.dto.TaskDto;
import org.example.taskbe.domain.entity.TaskEntity;
import org.example.taskbe.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskEntity sampleTaskEntity;
    private TaskDto sampleTaskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTaskEntity = new TaskEntity(
                1,
                Topic.MATHEMATICS,
                "Task 1",
                "Description 1",
                LocalDateTime.now().plusDays(1),
                false,
                LocalDateTime.now(),
                Priority.CRITICAL
        );

        sampleTaskDto = new TaskDto(
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
    void testGetAllTasks() {
        List<TaskEntity> tasks = Arrays.asList(sampleTaskEntity);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskEntity> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(sampleTaskEntity));

        Optional<TaskEntity> result = taskService.getById(1);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        Optional<TaskEntity> result = taskService.getById(999);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999);
    }

    @Test
    void testSaveTask() {
        when(taskRepository.create(any(TaskEntity.class))).thenReturn(sampleTaskEntity);

        TaskEntity result = taskService.saveTask(sampleTaskDto);

        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).create(any(TaskEntity.class));
    }

    @Test
    void testGetTaskByTitle() {
        List<TaskEntity> tasks = Arrays.asList(sampleTaskEntity);
        when(taskRepository.findByTitle("Task 1")).thenReturn(tasks);

        List<TaskEntity> result = taskService.getTaskByTitle("Task 1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        verify(taskRepository, times(1)).findByTitle("Task 1");
    }
}