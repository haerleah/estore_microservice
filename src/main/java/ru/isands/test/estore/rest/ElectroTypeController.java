package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ru.isands.test.estore.dto.ElectroTypeDTO;


@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Electro Item Type", description = "Сервис для выполнения операций над типами товаров")
@RequestMapping("/estore/api/item-type")
public class ElectroTypeController {
    private final EStoreDirectoryService directoryService;

    @Operation(
            summary = "Список типов товаров",
            description = "Возвращает список текущих типов товаров.\n " +
                    "Доступные параметры пагинации и сортировки:\n " +
                    "- `page` - индекс страницы (>= 0)\n " +
                    "- `size` - количество записей на страницу (>= 1)\n " +
                    "- `sort` - поле и направление сортировки, например `name, desc`\n " +
                    "Сортировка возможна по следующим полям: `id`, `name`"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ElectroTypeDTO.class)
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
    Page<ElectroTypeDTO> getElectroTypes(
            @ParameterObject
            @PageableDefault(size = 1000, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return directoryService.getElectroTypes(pageable);
    }

    @Operation(summary = "Сохранить тип товара")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполонен успешно, тип товара создан",
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
    ResponseEntity<Void> saveElectroItemType(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ElectroTypeDTO.class)
                    )
            )
            @RequestBody ElectroTypeDTO dto) {
        directoryService.saveElectroType(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить тип товара")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно, тип товара обновлен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тип товара с указанным ID не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    void updateElectroItemType(
            @Parameter(description = "Идентификатор типа товара")
            @PathVariable(name = "id") Long electroItemId,
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ElectroTypeDTO.class)
                    )
            )
            @RequestBody ElectroTypeDTO electroTypeDTO) {
        directoryService.updateElectroType(electroItemId, electroTypeDTO);
    }
}
