package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Метрика для ранжирования: количество или сумма продаж",
        example = "soldCount"
)
public enum Metric {
    @Schema(description = "Количество проданных товаров")
    soldCount,
    @Schema(description = "Сумма проданных товаров")
    soldSum
}
