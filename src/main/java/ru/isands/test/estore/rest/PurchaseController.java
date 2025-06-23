package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.domain.service.EStorePurchaseService;
import ru.isands.test.estore.dto.PurchaseCommitDTO;
import ru.isands.test.estore.dto.PurchaseDTO;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Purchase", description = "Сервис для выполнения операций над покупками магазина")
@RequestMapping("/estore/api/purchase")
public class PurchaseController {
    private final EStorePurchaseService purchaseService;

    @Operation(
            summary = "Получить список покупок (опциональная пагинация и сортировка)",
            description = "Возвращает список покупок. Доступные параметры пагинации и сортировки:\n " +
                    "- `page` - индекс страницы (>= 0)\n " +
                    "- `size` - количество записей на страницу (>= 1)\n " +
                    "- `sort` - поле и направление сортировки, например `purchaseDate, desc`\n " +
                    "Сортировка возможна по следующим полям: `id`, `electroItemName`, `employeeName`, " +
                    "`purchaseDate`, `shopName`, `purchaseType`"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PurchaseDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Передан page < 0 или size < 1 или указано некорректное поле для сортировки",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("")
    Page<PurchaseDTO> getPurchases(
            @ParameterObject
            @PageableDefault(size = 1000, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return purchaseService.getPurchases(pageable);
    }

    @Operation(summary = "Сохранить товар")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполонен успешно, товар создан",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Остаток товара в выбранном магазине <= 0 или " +
                            "сотрудник не работает в выбранном магазине или " +
                            "сотрудник не может продавать данный тип товара",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PostMapping("")
    ResponseEntity<Void> saveElectroItem(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PurchaseCommitDTO.class)
                    )
            )
            @RequestBody PurchaseCommitDTO purchaseCommitDTO) {
        purchaseService.savePurchase(purchaseCommitDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить покупку")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно, покупка обновлена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Покупка с указанным ID не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    void updatePurchase(
            @Parameter(description = "Идентификатор покупки")
            @PathVariable(name = "id") Long purchaseId,
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PurchaseCommitDTO.class)
                    )
            )
            @RequestBody PurchaseCommitDTO purchaseCommitDTO) {
        purchaseService.updatePurchase(purchaseId, purchaseCommitDTO);
    }
}
