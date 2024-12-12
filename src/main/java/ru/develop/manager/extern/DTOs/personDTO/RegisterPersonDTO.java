package ru.develop.manager.extern.DTOs.personDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Сущность для регистрации пользователя")
public class RegisterPersonDTO {

    @Schema(description = "Имя", example = "Алексей")
    private String firstName;
    @Schema(description = "Фамилия", example = "Штамов")
    private String lastName;
    @Schema(description = "Электронная почта", example = "shamov@mail.ru")
    private String email;
    @Schema(description = "Пароль", example = "Industrial2015!")
    private String password;
    @Schema(description = "Повторение пароля", example = "Industrial2015!")
    private String repeatPassword;
}
