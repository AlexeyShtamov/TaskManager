package ru.develop.manager.extern.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на получение jwt")
public class JwtRequest {

    @Schema(description = "Электронная почта", example = "alexshamox@gmail.com")
    private String email;
    @Schema(description = "Пароль", example = "Industrial2015!")
    private String password;
}
