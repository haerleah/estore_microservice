package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(
        name = "ElectroItemPutDTO",
        description = "DTO, содержащий обновленную информацию о товаре"
)
@AllArgsConstructor
@Data
public class ElectroItemPutDTO {
    @Schema(description = "Обновленная информация о товаре")
    @JsonUnwrapped
    private ElectroItemDTO item;
    @Schema(description = "Обновленная информация ою остатках товара")
    private List<StockDTO> stocks;
}
