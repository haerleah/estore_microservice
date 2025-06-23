package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(
        name = "EmployeePutDTO",
        description = "DTO, содержащий обновленную информацию о сотруднике"
)
@AllArgsConstructor
@Data
public class EmployeePutDTO {
    @Schema(description = "Обновленная информация о сотруднике")
    @JsonUnwrapped
    private EmployeeDTO employeeDTO;
    @Schema(description = "Список идентификаторов типа товара, которые сотрудник может продавать")
    private List<Long> availableElectroTypeIds;
}
