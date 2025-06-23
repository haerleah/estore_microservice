package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Schema(
        name = "PurchaseDTO",
        description = "DTO, инкапсулирующий необходимую информацию о покупке"
)
@Data
@AllArgsConstructor
public class PurchaseDTO {
    @Schema(description = "Идентификатор покупки", example = "1")
    private Long id;
    @Schema(description = "Идентификатор товара", example = "10")
    private Long electroItemId;
    @Schema(description = "Наименование товара", example = "Смартфон Nokia C1 Plus")
    private String electroItemName;
    @Schema(description = "Идентификатор сотрудника, продавшего товар", example = "1")
    private Long employeeId;
    @Schema(description = "ФИО сотрудника, продавшего товар", example = "Иванов Иван Иванович")
    private String employeeName;
    @Schema(description = "Дата и время покупки", example = "01.01.2001 15:40")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date purchaseDate;
    @Schema(description = "Идентификатор магазина, в котором проведена продажа", example = "1")
    private Long shopId;
    @Schema(description = "Название магазина, в котором проведена продажа", example = "Магазин-склад")
    private String shopName;
    @Schema(description = "Идентификатор способа оплаты", example = "5")
    private Long purchaseTypeId;
    @Schema(description = "Способ оплаты", example = "Кредит")
    private String purchaseType;
}
