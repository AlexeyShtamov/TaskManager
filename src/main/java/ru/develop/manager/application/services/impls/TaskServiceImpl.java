package ru.develop.manager.application.services.impls;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.application.services.TaskService;
import ru.develop.manager.domain.Comment;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.domain.enums.Priority;
import ru.develop.manager.domain.enums.Status;
import ru.develop.manager.extern.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Документацию смотреть в интерфейсах
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonService personService;

    @Transactional
    @Override
    public Task createTask(@Valid Task task, Person author, List<Long> executorsId) {

        if (executorsId == null) executorsId = new ArrayList<>();
        for (Long id : executorsId){
            Person executor = personService.findById(id).orElseThrow(() -> new NullPointerException("No executor with id " + id));
            task.getExecutors().add(executor);
        }

        task.setAuthor(author);


        if (task.getStatus() == null) task.setPriority(Priority.MEDIUM);
        if (task.getStatus() == null) task.setStatus(Status.APPOINTED);

        Task createdTask = taskRepository.save(task);
        log.info("TaskService: Task with title {} is created", task.getTitle());

        return createdTask;
    }

    @Transactional
    @Override
    public Task updateTaskInfo(Long id, String title, String description) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("No task with id " + id));
        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);

        Task updatedTask = taskRepository.save(task);
        log.info("TaskService: Task with id {} id updated", id);
        return updatedTask;
    }

    @Override
    public Task findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("No task with id " + id));
        Hibernate.initialize(task.getExecutors());
        log.info("TaskService: Task with id {} is found", id);
        return task;
    }

    @Override
    public Page<Task> findTaskByAuthorId(Long authorId,  Integer offset, Integer limit) {
        Person author = personService.findById(authorId)
                .orElseThrow(() -> new NullPointerException("No author with id " + authorId));
        Page<Task> tasks = taskRepository.findByAuthor(author, PageRequest.of(offset, limit));
        log.info("TaskService: Task page offset: {}, limit: {}, by author id {} is gotten", offset, limit, authorId);
        return tasks;
    }

    @Override
    public Page<Task> findTaskByExecutorId(Long executorId,  Integer offset, Integer limit) {
        Page<Task> tasks = taskRepository.findByExecutorsId(executorId, PageRequest.of(offset, limit));
        log.info("TaskService: Task page offset: {}, limit: {}, by executor id {} is gotten", offset, limit, executorId);
        return tasks;
    }


    @Override
    public Page<Task> findAllTasks(Integer offset, Integer limit) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(offset, limit));
        log.info("TaskService: Task page offset: {}, limit: {} is gotten", offset, limit);
        return tasks;
    }

    @Transactional
    @Override
    public Task addExecutors(List<Long> executorsId, Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("No task with id " + id));

        for (Long executorId : executorsId){
            Person executor = personService.findById(executorId).orElseThrow(() -> new NullPointerException("No executor with id " + executorId));
            task.getExecutors().add(executor);
        }
        Task updatedTask = taskRepository.save(task);
        log.info("TaskService: added executors in tas with id {}", id);
        return updatedTask;
    }

    @Transactional
    @Override
    public Task changeTaskPriority(String priority, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("No task with id " + id));
        try{
            task.setPriority(Priority.valueOf(priority.toUpperCase()));
        } catch (Exception e){
            throw new EnumConstantNotPresentException(Priority.class, priority);
        }
        Task updatedTask = taskRepository.save(task);
        log.info("TaskService: new priority {} in task with id {}", priority, id);
        return updatedTask;
    }

    @Transactional
    @Override
    public Task changeTaskStatus(String status, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("No task with id " + id));
        try{
            task.setStatus(Status.valueOf(status.toUpperCase()));
        } catch (Exception e){
            throw new EnumConstantNotPresentException(Priority.class, status);
        }
        Task updatedTask = taskRepository.save(task);
        log.info("TaskService: new status {} in task with    id {}", status, id);
        return updatedTask;
    }

    @Override
    public Task sendTaskComment(@Valid Comment comment, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("No task with id " + id));
        task.getComments().add(comment);
        comment.setTask(task);
        Task newCommentTask = taskRepository.save(task);
        log.info("TaskService: added new comment to task with id {}", id);
        return newCommentTask;
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
        log.warn("Task with id {} id deleted", id);
    }


}
