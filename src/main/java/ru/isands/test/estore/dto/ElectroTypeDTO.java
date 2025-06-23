package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
        name = "ElectroTypeDTO",
        description = "DTO, инкапсулирующий необходимую информацию о типах товаров"
)
@Data
@AllArgsConstructor
@Builder
public class ElectroTypeDTO {
    @Schema(description = "Идентификатор типа товара", example = "1")
    private Long id;
    @Schema(description = "Наименование типа товара", example = "Телевизоры")
    private String name;
}
