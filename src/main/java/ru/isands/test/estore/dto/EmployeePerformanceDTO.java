package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "EmployeePerformanceDTO",
        description = "DTO, инкапсулирующий необходимую информацию о произоводительности сотрудника"
)
@AllArgsConstructor
@Data
public class EmployeePerformanceDTO {
    @Schema(description = "Идентификатор сотрудника", example = "2")
    private long id;
    @Schema(description = "ФИО сотрудника, продавшего товар", example = "Иванов Иван Иванович")
    private String name;
    @Schema(description = "Сумма проданных товаров", example = "25660")
    private long soldSum;
    @Schema(description = "Количество проданных товаров", example = "10")
    private long soldCount;
}
