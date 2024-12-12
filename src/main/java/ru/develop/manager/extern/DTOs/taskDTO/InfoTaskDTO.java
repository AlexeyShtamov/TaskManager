package ru.develop.manager.extern.DTOs.taskDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.develop.manager.extern.DTOs.personDTO.InfoPersonDto;

import java.util.List;

@Data
@Schema(description = "Открытая информация задачи")
public class InfoTaskDTO {
    @Schema(description = "Итендификатор задачи", example = "1")
    private Long id;
    @Schema(description = "Название задачи", example = "Написание тестов")
    private String title;
    @Schema(description = "Описание задачи", example = "Необходимо написать unit тесты для контроллера пользователя")
    private String description;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private String priority;
    @Schema(description = "Статус задачи", example = "IN_PROGRESS")
    private String status;
    @Schema(description = "Список комментов к задаче", example = "{'Переделать', 'Норм', 'Молодец'}")
    private List<String> comments;
    @Schema(description = "DTO автора задачи")
    private InfoPersonDto author;
    @Schema(description = "DTO исполнителей задачи")
    private List<InfoPersonDto> executors;
}
