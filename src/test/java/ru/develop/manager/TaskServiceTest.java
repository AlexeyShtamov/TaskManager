package ru.develop.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.application.services.impls.TaskServiceImpl;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.domain.enums.Priority;
import ru.develop.manager.domain.enums.Status;
import ru.develop.manager.extern.repositories.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldCreateTaskWithDefaultValues() {
        // Arrange
        Task task = new Task();
        Person author = new Person();
        List<Long> executorIds = List.of(1L);
        Person executor = new Person();
        executor.setId(1L);

        when(personService.findById(1L)).thenReturn(Optional.of(executor));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(task, author, executorIds);

        // Assert
        assertNotNull(createdTask);
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(Status.APPOINTED, task.getStatus());
        assertEquals(author, task.getAuthor());
        assertTrue(task.getExecutors().contains(executor));
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTaskInfo_shouldUpdateTaskTitleAndDescription() {
        // Arrange
        Long taskId = 1L;
        String newTitle = "Updated Title";
        String newDescription = "Updated Description";
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task updatedTask = taskService.updateTaskInfo(taskId, newTitle, newDescription);

        // Assert
        assertEquals(newTitle, updatedTask.getTitle());
        assertEquals(newDescription, updatedTask.getDescription());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void findTaskById_shouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        Task foundTask = taskService.findTaskById(taskId);

        // Assert
        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void findTaskByAuthorId_shouldReturnTasksByAuthor() {
        // Arrange
        Long authorId = 1L;
        Person author = new Person();
        author.setId(authorId);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(personService.findById(authorId)).thenReturn(Optional.of(author));
        when(taskRepository.findByAuthor(author, pageRequest)).thenReturn(tasks);

        // Act
        Page<Task> result = taskService.findTaskByAuthorId(authorId, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(taskRepository, times(1)).findByAuthor(author, pageRequest);
    }

    @Test
    void addExecutors_shouldAddExecutorsToTask() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        List<Long> executorIds = List.of(2L);
        Person executor = new Person();
        executor.setId(2L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(personService.findById(2L)).thenReturn(Optional.of(executor));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task updatedTask = taskService.addExecutors(executorIds, taskId);

        // Assert
        assertNotNull(updatedTask);
        assertTrue(task.getExecutors().contains(executor));
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void changeTaskPriority_shouldChangePriority() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        String newPriority = "HIGH";

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task updatedTask = taskService.changeTaskPriority(newPriority, taskId);

        // Assert
        assertNotNull(updatedTask);
        assertEquals(Priority.HIGH, task.getPriority());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTaskById_shouldDeleteTask() {
        // Arrange
        Long taskId = 1L;

        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.deleteTaskById(taskId);

        // Assert
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}

