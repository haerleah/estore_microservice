package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Schema(
        name = "PurchaseCommitDTO",
        description = "DTO, содержащий необходимую информацию, для создания и изменения покупки"
)
@Data
@AllArgsConstructor
public class PurchaseCommitDTO {
    @Schema(description = "Идентификатор товара", example = "10")
    private Long electroItemId;
    @Schema(description = "Идентификатор сотрудника, продавшего товар", example = "1")
    private Long employeeId;
    @Schema(description = "Дата и время покупки", example = "1999-12-31'T'12:42")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date purchaseDate;
    @Schema(description = "Идентификатор магазина, в котором проведена продажа", example = "1")
    private Long shopId;
    @Schema(description = "Идентификатор способа оплаты", example = "5")
    private Long purchaseTypeId;
}
