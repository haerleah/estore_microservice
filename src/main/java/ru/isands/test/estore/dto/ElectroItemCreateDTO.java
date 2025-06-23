package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Schema(
        name = "ElectroItemCreateDTO",
        description = "DTO, содержащий необходимую информацию, для создания товара"
)
@Data
@AllArgsConstructor
public class ElectroItemCreateDTO {
    @Schema(description = "Название товара", example = "Смартфон Nokia C1 Plus")
    private String name;
    @Schema(description = "Идентификатор типа товара", example = "3")
    private Long electroTypeId;
    @Schema(description = "Признак архивности товара (true - нет в наличии, false - в продаже)", example = "true")
    private Boolean archive;
    @Schema(description = "Цена товара", example = "10999")
    private long price;
    @Schema(description = "Описание товара",
            example = "32 ГБ золотистый, 2000x1200, TFT PLS, 8х1.8 ГГц, 2 ГГц, 3 ГБ, BT, GPS, 7040 мА*ч, Android 10.x")
    private String description;
    @Schema(description = "Количество товара по магазина")
    @Size(min = 1)
    private List<StockDTO> stocks;
}
