package ru.develop.manager.extern.DTOs.personDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Список id исполнителей")
public class ExecutorsDTO {

    @Schema(description = "Список id", example = "{1, 2, 3}")
    private List<Long> executorsId = new ArrayList<>();
}
