package org.example.taskbe.repository;

import org.example.taskbe.domain.Priority;
import org.example.taskbe.domain.Topic;
import org.example.taskbe.domain.entity.TaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    private TaskRepository taskRepository;

    private TaskEntity sampleTaskEntity;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();
        sampleTaskEntity = new TaskEntity(
                null,
                Topic.MATHEMATICS,
                "Task 1",
                "Description 1",
                LocalDateTime.now().plusDays(1),
                false,
                LocalDateTime.now(),
                Priority.CRITICAL
        );
    }

    @Test
    void testFindAll_EmptyInitially() {
        List<TaskEntity> tasks = taskRepository.findAll();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testCreateTask() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);

        assertNotNull(createdTask);
        assertEquals(0, createdTask.getId());
        assertEquals("Task 1", createdTask.getTitle());

        List<TaskEntity> allTasks = taskRepository.findAll();
        assertEquals(1, allTasks.size());
        assertEquals(createdTask, allTasks.get(0));
    }

    @Test
    void testFindById() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);

        Optional<TaskEntity> foundTask = taskRepository.findById(createdTask.getId());

        assertTrue(foundTask.isPresent());
        assertEquals(createdTask, foundTask.get());
    }

    @Test
    void testFindByTitle() {
        taskRepository.create(sampleTaskEntity);
        taskRepository.create(new TaskEntity(
                null,
                Topic.ENGLISH,
                "Another Task",
                "Description 2",
                LocalDateTime.now().plusDays(2),
                false,
                LocalDateTime.now(),
                Priority.MAJOR
        ));

        List<TaskEntity> tasksWithTitle = taskRepository.findByTitle("Task");

        assertNotNull(tasksWithTitle);
        assertEquals(2, tasksWithTitle.size());
        assertTrue(tasksWithTitle.stream().allMatch(task -> task.getTitle().contains("Task")));
    }

    @Test
    void testFindByTitle_NoMatch() {
        taskRepository.create(sampleTaskEntity);

        List<TaskEntity> tasksWithTitle = taskRepository.findByTitle("Nonexistent");

        assertNotNull(tasksWithTitle);
        assertTrue(tasksWithTitle.isEmpty());
    }
}