package ru.develop.manager.extern.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Для загрузки комментария задаче")
public class CommentDTO {

    @Schema(description = "Текст коммента", example = "Хааахахаха неплохо")
    private String text;
}
