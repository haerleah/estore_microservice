package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
        name = "ShopDTO",
        description = "DTO, инкапсулирующий необходимую информацию о магазине"
)
@Data
@AllArgsConstructor
@Builder
public class ShopDTO {
    @Schema(description = "Идентификатор магазина", example = "1")
    private Long id;
    @Schema(description = "Название магазина", example = "Магазин-склад")
    private String name;
    @Schema(description = "Название магазина", example = "г.Новосибирск, ул. Фабричная, д.2")
    private String address;
}
