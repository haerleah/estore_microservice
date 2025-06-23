package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "BestEmployeeDTO",
        description = "DTO, инкапсулирующий необходимую информацию о лучшем сотруднике, " +
                "по количеству продаж."
)
@AllArgsConstructor
@Data
public class BestEmployeeDTO {
    @Schema(description = "Идентификатор сотрудника", example = "1")
    private Long id;
    @Schema(description = "ФИО сотрудника, продавшего товар", example = "Иванов Иван Иванович")
    private String name;
    @Schema(description = "Должность сотрудника", example = "Продавец-консультант")
    private String position;
    @Schema(description = "Название магазина, в котором работает сотрудник", example = "Магазин-склад")
    private String shopName;
    @Schema(description = "Количество проданных товаров", example = "10")
    private long soldCount;
}
