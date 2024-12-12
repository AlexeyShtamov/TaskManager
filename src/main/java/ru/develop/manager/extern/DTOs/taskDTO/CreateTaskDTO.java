package ru.develop.manager.extern.DTOs.taskDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Сущность для создания задачи")
public class CreateTaskDTO {
    @Schema(description = "Название задачи", example = "Написание тестов")
    private String title;
    @Schema(description = "Описание задачи", example = "Необходимо написать unit тесты для контроллера пользователя")
    private String description;
    @Schema(description = "Список id исполнителей", example = "{1, 2, 3}")
    private List<Long> executorsId;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private String priority;
    @Schema(description = "Статус задачи", example = "IN_PROGRESS")
    private String status;
}
