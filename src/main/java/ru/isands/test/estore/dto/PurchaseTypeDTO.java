package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
        name = "PurchaseTypeDTO",
        description = "DTO, инкапсулирующий необходимую информацию о способе оплаты"
)
@Data
@AllArgsConstructor
@Builder
public class PurchaseTypeDTO {
    @Schema(description = "Идентификатор способа оплаты", example = "5")
    private Long id;
    @Schema(description = "Название способа оплаты", example = "Кредит")
    private String name;
}
