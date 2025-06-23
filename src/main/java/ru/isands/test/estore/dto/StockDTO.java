package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "StockDTO",
        description = "DTO, отражающий количество создаваемого товара в определенном магазине"
)
@Data
@AllArgsConstructor
public class StockDTO {
    @Schema(description = "Идентификатор магазина", example = "1")
    private Long shopId;
    @Schema(description = "Количество товара", example = "15")
    private Integer count;
}
