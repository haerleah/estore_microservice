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
import ru.isands.test.estore.domain.service.EStoreDirectoryService;
import ru.isands.test.estore.domain.service.EStoreShopService;
import ru.isands.test.estore.dto.*;
import ru.isands.test.estore.exception.ResourceNotFoundException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Shop", description = "Сервис для выполнения операций над магазинами")
@RequestMapping("/estore/api/shop")
public class ShopController {
    private final EStoreShopService shopService;
    private final EStoreDirectoryService directoryService;

    @Operation(
            summary = "Список магазинов",
            description = "Возвращает список текущих магазиов.\n " +
                    "Доступные параметры пагинации и сортировки:\n " +
                    "- `page` - индекс страницы (>= 0)\n " +
                    "- `size` - количество записей на страницу (>= 1)\n " +
                    "- `sort` - поле и направление сортировки, например `name, desc`\n " +
                    "Сортировка возможна по следующим полям: `id`, `name`, `address`"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShopDTO.class)
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
    Page<ShopDTO> getShops(
            @ParameterObject
            @PageableDefault(size = 1000, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return directoryService.getShops(pageable);
    }

    @Operation(summary = "Сохранить магазин")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполонен успешно, магазин создан",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PostMapping("")
    ResponseEntity<Void> saveShop(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ShopDTO.class)
                    )
            )
            @RequestBody ShopDTO dto) {
        directoryService.saveShop(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить магазин")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно, магазин обновлен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Магазин с указанным ID не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    void updateShop(
            @Parameter(description = "Идентификатор покупки")
            @PathVariable(name = "id") Long shopId,
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ShopDTO.class)
                    )
            )
            @RequestBody ShopDTO shopDTO) {
        directoryService.updateShop(shopId, shopDTO);
    }

    @Operation(
            summary = "Получить остаток товара в магазине (проверить наличие определенного товара в магазине)",
            description = "Возвращает количество товара с указанным ID в магазине с указанным ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AvailabilityDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нет магазина и/или товара с указанными идентификаторами",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("/{shopId}/items/{itemId}/availability")
    public AvailabilityDTO getAvailability(@PathVariable Long shopId,
                                           @PathVariable Long itemId) throws ResourceNotFoundException {
        return shopService.getAvailability(shopId, itemId);
    }

    @Operation(
            summary = "Сумма платежей с опредленным способом оплаты",
            description = "Возвращает полную сумму платежей в указанном магазине, " +
                    "с указанным идентификатором типа оплаты"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentSummaryDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден магазин или указан неверный тип оплаты",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("/{shopId}/payments/{purchaseTypeId}/total")
    public PaymentSummaryDTO getPaymentSummary(
            @Parameter(description = "Идентификатор магазина", example = "1")
            @PathVariable Long shopId,
            @Parameter(description = "Идентификатор способа оплаты", example = "4")
            @PathVariable Long purchaseTypeId) {
        return shopService.getPaymentSummary(shopId, purchaseTypeId);
    }
}
