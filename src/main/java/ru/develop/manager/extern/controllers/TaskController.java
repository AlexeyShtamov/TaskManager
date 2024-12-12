package ru.develop.manager.extern.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.develop.manager.application.services.PersonService;
import ru.develop.manager.application.services.TaskService;
import ru.develop.manager.domain.Comment;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;
import ru.develop.manager.domain.enums.Role;
import ru.develop.manager.extern.DTOs.CommentDTO;
import ru.develop.manager.extern.DTOs.personDTO.ExecutorsDTO;
import ru.develop.manager.extern.DTOs.taskDTO.CreateTaskDTO;
import ru.develop.manager.extern.DTOs.taskDTO.InfoTaskDTO;
import ru.develop.manager.extern.assemblers.TaskAssembler;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "task_methods")
public class TaskController {
    private final TaskService taskService;
    private final PersonService personService;
    private final TaskAssembler taskAssembler;

    @Operation(summary = "Создание задачи",
            description = "Для создания необходимо в теле запроса передать CreateTaskDTO")
    @PostMapping
    public ResponseEntity<InfoTaskDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO, @AuthenticationPrincipal UserDetails userDetails){
        Person author = personService.findByEmail(userDetails.getUsername()).orElseThrow(NullPointerException::new);
        Task task = taskAssembler.fromDTOToTask(createTaskDTO);
        Task createdTask = taskService.createTask(task, author, createTaskDTO.getExecutorsId());
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(createdTask), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление задачи по id",
            description = "Для обновленя задачи необходимо в RequestParam передать title и description." +
                    "Можно ничего не передавать, в там случае название или описание останется прежним")
    @PutMapping("/{id}")
    public ResponseEntity<InfoTaskDTO> updateTaskInfo(@PathVariable Long id,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("description") String description){
        Task updatedTask = taskService.updateTaskInfo(id, title, description);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Возвращение задачи по id",
            description = "Необходимо в url передать id задачи")
    @GetMapping("/{id}")
    public ResponseEntity<InfoTaskDTO> findById(@PathVariable Long id){
        Task foundedTask = taskService.findTaskById(id);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(foundedTask), HttpStatus.OK);
    }

    @Operation(summary = "Возвращение страницы задач по id автора",
            description = "Необходимо в url передать id автора, а в качестве request param:" +
                    "offset - номер страницы (счет от 0); limit - количество задач на 1 странице")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<InfoTaskDTO>> findTaskByAuthorId(@PathVariable Long authorId,
                                                                @RequestParam("offset")  Integer offset,
                                                                @RequestParam("limit") Integer limit){

        Page<Task> taskPage = taskService.findTaskByAuthorId(authorId, offset, limit);
        Page<InfoTaskDTO> dtoPage = taskPage.map(taskAssembler::fromTaskToDTO);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @Operation(summary = "Возвращение страницы задач по id исполнителя",
            description = "Необходимо в url передать id исполнителя, а в качестве request param:" +
                    "offset - номер страницы (счет от 0); limit - количество задач на 1 странице")
    @GetMapping("/executor/{executorId}")
    public ResponseEntity<Page<InfoTaskDTO>> findTaskByExecutorId(@PathVariable Long executorId,
                                                                  @RequestParam("offset")  Integer offset,
                                                                  @RequestParam("limit") Integer limit,
                                                                  @AuthenticationPrincipal UserDetails userDetails){
        Person person = personService.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!person.getRole().equals(Role.ROLE_ADMIN) && !person.getId().equals(executorId))
            throw new PermissionDeniedDataAccessException("No permission to executor's tasks with id " + executorId, new Throwable());

        Page<Task> taskPage = taskService.findTaskByExecutorId(executorId, offset, limit);
        Page<InfoTaskDTO> dtoPage = taskPage.map(taskAssembler::fromTaskToDTO);

        return new ResponseEntity<>(dtoPage, HttpStatus.FOUND);
    }

    @Operation(summary = "Возвращение страницы задач",
            description = "Чтобы получить страницу с несколькими задачами неоходимо указать:" +
                    "offset - номер страницы (счет от 0); limit - количество задач на 1 странице")
    @GetMapping
    public ResponseEntity<Page<InfoTaskDTO>> findAllTasks(@RequestParam("offset")  Integer offset,
                                                          @RequestParam("limit") Integer limit) {
        Page<Task> taskPage = taskService.findAllTasks(offset, limit);
        Page<InfoTaskDTO> dtoPage = taskPage.map(taskAssembler::fromTaskToDTO);

        return new ResponseEntity<>(dtoPage, HttpStatus.FOUND);
    }

    @Operation(summary = "Изменение приоритета задачи",
            description = "Необходимо в url передать id задачи, а в качестве request param новый приоритет")
    @PutMapping("/priority/{id}")
    public ResponseEntity<InfoTaskDTO> changeTaskPriority(@RequestParam("priority") String priority,
                                                          @PathVariable Long id) {
        Task updatedTask = taskService.changeTaskPriority(priority, id);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Изменение статуса задачи",
            description = "Необходимо в url передать id задачи, а в качестве request param новый статус")
    @PutMapping("/status/{id}")
    public ResponseEntity<InfoTaskDTO> changeTaskStatus(@RequestParam("status")String status,
                                                        @PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        Person person = personService.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!person.getRole().equals(Role.ROLE_ADMIN) && !personService.getTasksId(person.getId()).contains(id))
            throw new PermissionDeniedDataAccessException("No permission to executor's tasks with id " + person.getId(), new Throwable());

        Task updatedTask = taskService.changeTaskStatus(status, id);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Отправление комментария к задаче",
            description = "Необходимо в url передать id задачи, а телом запроса DTO комментария")
    @PutMapping("/comment/{id}")
    public ResponseEntity<InfoTaskDTO> sendTaskComment(@RequestBody CommentDTO commentDTO,
                                                       @PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        Person person = personService.findByEmail(userDetails.getUsername()).orElseThrow();
        if (!person.getRole().equals(Role.ROLE_ADMIN) && !personService.getTasksId(person.getId()).contains(id))
            throw new PermissionDeniedDataAccessException("No permission to executor's tasks with id " + person.getId(), new Throwable());

        Comment comment = new Comment(commentDTO.getText());
        Task commentedTask = taskService.sendTaskComment(comment, id);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(commentedTask), HttpStatus.OK);
    }

    @Operation(summary = "Добавление исполнителей к задачи",
            description = "Необходимо в url передать id задачи, а телом запроса DTO списка id пользователей, " +
                    "которых хотите сделать исполнителями")
    @PutMapping("/executor/{id}")
    public ResponseEntity<InfoTaskDTO> addExecutors(@RequestBody ExecutorsDTO executorsDTO,
                                                    @PathVariable Long id){

        Task updatedTask = taskService.addExecutors(executorsDTO.getExecutorsId(), id);
        return new ResponseEntity<>(taskAssembler.fromTaskToDTO(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Удаление задачи по id",
            description = "Необходимо в url передать id задачи, которую хотите удалить")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
