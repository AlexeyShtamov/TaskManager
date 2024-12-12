package ru.develop.manager.extern.DTOs.personDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Открытая информация пользователя")
public class InfoPersonDto {
    @Schema(description = "Имя", example = "Алексей")
    private String firstName;
    @Schema(description = "Фамилия", example = "Штамов")
    private String lastName;
    @Schema(description = "Электронная почта", example = "alexshamox@gmail.com")
    private String email;
}
