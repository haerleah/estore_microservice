package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "PaymentSummaryDTO",
        description = "DTO, инкапсулирующий необходимую информацию о сумме оплат определнным способом," +
                "в конкретном магазине"
)
@AllArgsConstructor
@Data
public class PaymentSummaryDTO {
    @Schema(description = "Идентификатор магазина", example = "1")
    private Long shopId;
    @Schema(description = "Идентификатор способа оплаты", example = "5")
    private Long purchaseTypeId;
    @Schema(description = "Название магазина", example = "Магазин-склад")
    private String shopName;
    @Schema(description = "Название способа оплаты", example = "Кредит")
    private String purchaseTypeName;
    @Schema(description = "Сумма оплат", example = "25899")
    private Long totalAmount;
}
