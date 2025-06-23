package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Schema(
        name = "EmployeeDTO",
        description = "DTO, инкапсулирующий необходимую информацию о сотруднике"
)
@Data
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    @Schema(description = "Идентификатор сотрудника", example = "1")
    private Long id;
    @Schema(description = "Имя сотрудника", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия сотрудника", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество сотрудника", example = "Иванович")
    private String patronymic;
    @Schema(description = "Дата рождения сотрудника", example = "01.01.1999")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date birthDate;
    @Schema(description = "Идентификатор должности сотрудника", example = "3")
    private Long positionId;
    @Schema(description = "Должность сотрудника", example = "Продавец-консультант")
    private String position;
    @Schema(description = "Идентификатор магазина, в котором работает сотрудник", example = "1")
    private Long shopId;
    @Schema(description = "Название магазина, в котором работает сотрудник", example = "Магазин-склад")
    private String shopName;
    @Schema(description = "Пол сотрудника: true — мужской, false — женский", example = "true")
    private Boolean gender;
}
