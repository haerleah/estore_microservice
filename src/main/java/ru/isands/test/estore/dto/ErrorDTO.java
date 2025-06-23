package ru.isands.test.estore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "ErrorDTO",
        description = "DTO, инкапсулирующий информацию об ошибке"
)
@AllArgsConstructor
@Data
public class ErrorDTO {
    @Schema(description = "HTTP код ошибки", example = "500")
    private int code;
    @Schema(description = "Сообщение ошибки", example = "Internal server error")
    private String message;
}
