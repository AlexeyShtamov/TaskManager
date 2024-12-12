package ru.develop.manager.application.services;

import org.springframework.data.domain.Page;
import ru.develop.manager.domain.Comment;
import ru.develop.manager.domain.Person;
import ru.develop.manager.domain.Task;

import java.util.List;

public interface TaskService {

    /**
     * Метод сохранения новой задачи в бд
     * @param task Сущность задачи
     * @param author Автор задачи (Person)
     * @param executorsId Список ID исполнителей задачи
     * @return Созданную задачу
     */
    Task createTask(Task task, Person author, List<Long> executorsId);

    /**
     * Метод для обновления названия и описания существующей задачи
     * @param id ID задачи
     * @param title Новое название задачи
     * @param description Новое описание задачи
     * @return Обновленную задачу
     */
    Task updateTaskInfo(Long id, String title, String description);

    /**
     * Нахождение задачи по ее ID
     * @param id ID задачи
     * @return Сущность задачи с данным ID
     */
    Task findTaskById(Long id);

    /**
     * Нахождение задачи по id ее создателя
     * @param id Итендификатор создателя
     * @param offset Номер страницы
     * @param limit размер страницы
     * @return Страницу (Page) задачи
     */
    Page<Task> findTaskByAuthorId(Long id, Integer offset, Integer limit);

    /**
     * Нахождение задачи по id ее исполнителя
     * @param executorId Итендификатор исполнителя
     * @param offset Номер страницы
     * @param limit размер страницы
     * @return Страницу (Page) задачи
     */
    Page<Task> findTaskByExecutorId(Long executorId,  Integer offset, Integer limit);


    /**
     * Получение всех задач (страницы задач)
     * @param offset Номер страницы
     * @param limit размер страницы
     * @return  Страницу (Page) задачи
     */
    Page<Task> findAllTasks(Integer offset, Integer limit);


    /**
     * Добавление исполнителя к задачи
     * @param executorsId Итендификатор исполнителя
     * @param id Итендификатор задачи
     * @return Задачу, к которой добавили исполнителя
     */
    Task addExecutors(List<Long> executorsId, Long id);

    /**
     * Изменение приоритета задачи
     * @param priority Новый приоритет
     * @param id Итендификатор задачи
     * @return Задачу с измененным приоритетом
     */
    Task changeTaskPriority(String priority, Long id);

    /**
     * Изменение статуса задачи
     * @param status Новый статус
     * @param id Итендификатор задачи
     * @return Задачу с измененным статусом
     */
    Task changeTaskStatus(String status, Long id);

    /**
     * Отправка комментария к задаче
     * @param comment Сам комментарий
     * @param id Итендификатор задачи
     * @return Задачу, к которой добавили коммент
     */
    Task sendTaskComment(Comment comment, Long id);

    /**
     * Удаление задачи на ее итендификатору
     * @param id Итендификатор задачи
     */
    void deleteTaskById(Long id);

}
