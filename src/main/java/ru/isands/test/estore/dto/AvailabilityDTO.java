package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "AvailabilityDTO",
        description = "DTO, отражающий количество товара в определнном магазине"
)
@Data
@AllArgsConstructor
public class AvailabilityDTO {
    @Schema(description = "Остаток товара в магазине", example = "42")
    private int quantity;
}
