package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "ElectroItemDTO",
        description = "DTO, инкапсулирующий необходимую информацию о товаре"
)
@Data
@AllArgsConstructor
public class ElectroItemDTO {
    @Schema(description = "Идентификатор товара", example = "1")
    private Long id;
    @Schema(description = "Наименование товара", example = "Смартфон Nokia C1 Plus")
    private String name;
    @Schema(description = "Идентификатор типа товара", example = "3")
    private Long electroTypeId;
    @Schema(description = "Тип товара", example = "Смартфоны")
    private String type;
    @Schema(description = "Цена товара", example = "10999")
    private long price;
    @Schema(description = "Количество товара", example = "10")
    private long count;
    @Schema(description = "Признак архивности товара (true - нет в наличии, false - в продаже)", example = "true")
    private boolean archive;
    @Schema(description = "Описание товара",
            example = "32 ГБ золотистый, 2000x1200, TFT PLS, 8х1.8 ГГц, 2 ГГц, 3 ГБ, BT, GPS, 7040 мА*ч, Android 10.x")
    private String description;
}
