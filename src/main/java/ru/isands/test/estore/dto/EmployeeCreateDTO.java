package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(
        name = "EmployeeCreateDTO",
        description = "DTO, содержащий необходимую информацию, для создания сотрудника"
)
@Data
@AllArgsConstructor
public class EmployeeCreateDTO {
    @Schema(description = "Имя сотрудника", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия сотрудника", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество сотрудника", example = "Иванович")
    private String patronymic;
    @Schema(description = "Дата рождения сотрудника", example = "1999-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    @Schema(description = "Идентификатор должности сотрудника", example = "3")
    private Long positionId;
    @Schema(description = "Идентификатор магазина, в котором работает сотрудник", example = "1")
    private Long shopId;
    @Schema(description = "Пол сотрудника: true — мужской, false — женский", example = "true")
    private Boolean gender;
    @Schema(description = "Список идентификаторов типа товара, которые сотрудник может продавать")
    private List<Long> availableElectroTypeIds;
}
