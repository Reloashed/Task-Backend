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
    void testFindAll_InitiallyEmpty() {
        List<TaskEntity> tasks = taskRepository.findAll();
        assertNotNull(tasks, "findAll should not return null");
        assertTrue(tasks.isEmpty(), "findAll should return an empty list initially");
    }

    @Test
    void testCreateTask() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);

        assertNotNull(createdTask, "Created task should not be null");
        assertEquals(0, createdTask.getId(), "First task should have ID 0");
        assertEquals("Task 1", createdTask.getTitle(), "Task title should match");

        List<TaskEntity> allTasks = taskRepository.findAll();
        assertEquals(1, allTasks.size(), "findAll should return one task after creation");
        assertEquals(createdTask, allTasks.get(0), "findAll should return the created task");
    }

    @Test
    void testFindById_ExistingTask() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);

        Optional<TaskEntity> foundTask = taskRepository.findById(createdTask.getId());

        assertTrue(foundTask.isPresent(), "findById should return a task when ID exists");
        assertEquals(createdTask, foundTask.get(), "Returned task should match the created task");
    }

    @Test
    void testFindById_IndexOutOfBounds() {
        Executable findInvalidId = () -> taskRepository.findById(-1);

        assertThrows(IndexOutOfBoundsException.class, findInvalidId, "Negative ID should throw IndexOutOfBoundsException");
    }

    @Test
    void testFindByTitle_MatchingTasks() {
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

        assertNotNull(tasksWithTitle, "findByTitle should not return null");
        assertEquals(2, tasksWithTitle.size(), "findByTitle should return two matching tasks");
        assertTrue(tasksWithTitle.stream().allMatch(task -> task.getTitle().contains("Task")), "All tasks should match the title criteria");
    }

    @Test
    void testFindByTitle_NoMatch() {
        taskRepository.create(sampleTaskEntity);

        List<TaskEntity> tasksWithTitle = taskRepository.findByTitle("Nonexistent");

        assertNotNull(tasksWithTitle, "findByTitle should not return null");
        assertTrue(tasksWithTitle.isEmpty(), "findByTitle should return an empty list for no matches");
    }

    @Test
    void testDeleteTask() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);
        taskRepository.create(new TaskEntity(
                null,
                Topic.MATHEMATICS,
                "Task 2",
                "Description 2",
                LocalDateTime.now().plusDays(3),
                false,
                LocalDateTime.now(),
                Priority.MINOR
        ));

        taskRepository.delete(createdTask.getId());

        List<TaskEntity> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size(), "One task should remain after deletion");
        assertEquals("Task 2", tasks.get(0).getTitle(), "Remaining task should have title 'Task 2'");
        assertEquals(0, tasks.get(0).getId(), "Remaining task ID should be reset to 0");
    }

    @Test
    void testUpdateExistingTask() {
        TaskEntity createdTask = taskRepository.create(sampleTaskEntity);
        createdTask.setTitle("Updated Task");
        createdTask.setDescription("Updated Description");

        TaskEntity updatedTask = taskRepository.create(createdTask);

        assertNotNull(updatedTask, "Updated task should not be null");
        assertEquals(createdTask.getId(), updatedTask.getId(), "Updated task ID should remain the same");
        assertEquals("Updated Task", updatedTask.getTitle(), "Title should be updated");
        assertEquals("Updated Description", updatedTask.getDescription(), "Description should be updated");

        List<TaskEntity> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size(), "findAll should return one task after update");
        assertEquals(updatedTask, tasks.get(0), "findAll should return the updated task");
    }
}
