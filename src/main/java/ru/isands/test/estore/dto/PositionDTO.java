package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
        name = "PositionDTO",
        description = "DTO, инкапсулирующий необходимую информацию о должности"
)
@Data
@AllArgsConstructor
@Builder
public class PositionDTO {
    @Schema(description = "Идентификатор должности", example = "1")
    private Long id;
    @Schema(description = "Название должности", example = "Стажер")
    private String name;
}
