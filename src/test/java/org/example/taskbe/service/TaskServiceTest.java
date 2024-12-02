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
import java.util.Collections;
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

        assertNotNull(result, "getAllTasks should not return null");
        assertEquals(1, result.size(), "getAllTasks should return one task");
        assertEquals("Task 1", result.get(0).getTitle(), "The task title should match");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasks_EmptyList() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<TaskEntity> result = taskService.getAllTasks();

        assertNotNull(result, "getAllTasks should not return null");
        assertTrue(result.isEmpty(), "getAllTasks should return an empty list");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetById_ExistingTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(sampleTaskEntity));

        Optional<TaskEntity> result = taskService.getById(1);

        assertTrue(result.isPresent(), "getById should return a task when ID exists");
        assertEquals("Task 1", result.get().getTitle(), "The task title should match");
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        Optional<TaskEntity> result = taskService.getById(999);

        assertFalse(result.isPresent(), "getById should return empty for non-existing ID");
        verify(taskRepository, times(1)).findById(999);
    }

    @Test
    void testGetById_IndexOutOfBounds() {
        when(taskRepository.findById(-1)).thenThrow(IndexOutOfBoundsException.class);

        assertThrows(IndexOutOfBoundsException.class, () -> taskService.getById(-1), "getById should throw IndexOutOfBoundsException for invalid ID");
        verify(taskRepository, times(1)).findById(-1);
    }

    @Test
    void testSaveTask() {
        when(taskRepository.create(any(TaskEntity.class))).thenReturn(sampleTaskEntity);

        TaskEntity result = taskService.saveTask(sampleTaskDto);

        assertNotNull(result, "saveTask should not return null");
        assertEquals("Task 1", result.getTitle(), "The saved task title should match");
        verify(taskRepository, times(1)).create(any(TaskEntity.class));
    }

    @Test
    void testSaveTask_WithNullFields() {
        TaskDto incompleteTaskDto = new TaskDto(
                null,
                Topic.MATHEMATICS,
                null, // Missing title
                null, // Missing description
                LocalDateTime.now().plusDays(1),
                false,
                null,
                Priority.CRITICAL
        );

        when(taskRepository.create(any(TaskEntity.class))).thenReturn(sampleTaskEntity);

        TaskEntity result = taskService.saveTask(incompleteTaskDto);

        assertNotNull(result, "saveTask should not return null for incomplete TaskDto");
        verify(taskRepository, times(1)).create(any(TaskEntity.class));
    }

    @Test
    void testGetTaskByTitle_MatchingTasks() {
        List<TaskEntity> tasks = Arrays.asList(sampleTaskEntity);
        when(taskRepository.findByTitle("Task 1")).thenReturn(tasks);

        List<TaskEntity> result = taskService.getTaskByTitle("Task 1");

        assertNotNull(result, "getTaskByTitle should not return null");
        assertEquals(1, result.size(), "getTaskByTitle should return one matching task");
        assertEquals("Task 1", result.get(0).getTitle(), "The task title should match");
        verify(taskRepository, times(1)).findByTitle("Task 1");
    }

    @Test
    void testGetTaskByTitle_NoMatch() {
        when(taskRepository.findByTitle("Nonexistent")).thenReturn(Collections.emptyList());

        List<TaskEntity> result = taskService.getTaskByTitle("Nonexistent");

        assertNotNull(result, "getTaskByTitle should not return null");
        assertTrue(result.isEmpty(), "getTaskByTitle should return an empty list for no matches");
        verify(taskRepository, times(1)).findByTitle("Nonexistent");
    }

    @Test
    void testEditTask() {
        TaskDto updatedTaskDto = new TaskDto(
                null,
                Topic.ENGLISH,
                "Updated Task",
                "Updated Description",
                LocalDateTime.now().plusDays(2),
                true,
                null,
                Priority.MINOR
        );

        when(taskRepository.create(any(TaskEntity.class))).thenReturn(TaskEntity.toEntity(updatedTaskDto));

        TaskEntity result = taskService.editTask(updatedTaskDto, 1);

        assertNotNull(result, "editTask should not return null");
        assertEquals("Updated Task", result.getTitle(), "The updated task title should match");
        verify(taskRepository, times(1)).create(any(TaskEntity.class));
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).delete(1);

        assertDoesNotThrow(() -> taskService.deleteTask(1), "deleteTask should not throw an exception");
        verify(taskRepository, times(1)).delete(1);
    }

    @Test
    void testDeleteTask_NonExisting() {
        doNothing().when(taskRepository).delete(999);

        assertDoesNotThrow(() -> taskService.deleteTask(999), "deleteTask should not throw an exception for non-existing ID");
        verify(taskRepository, times(1)).delete(999);
    }
}
