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
import ru.isands.test.estore.domain.service.EStoreEmployeeService;
import ru.isands.test.estore.dto.BestEmployeeDTO;
import ru.isands.test.estore.dto.PositionDTO;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Position", description = "Сервис для выполнения операций над должностями")
@RequestMapping("/estore/api/position")
public class PositionController {
    private final EStoreEmployeeService employeeService;
    private final EStoreDirectoryService directoryService;

    @Operation(
            summary = "Список должностей",
            description = "Возвращает список текущих должностей.\n " +
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
                            schema = @Schema(implementation = PositionDTO.class)
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
    Page<PositionDTO> getPositions(
            @ParameterObject
            @PageableDefault(size = 1000, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return directoryService.getPositions(pageable);
    }

    @Operation(summary = "Сохранить должность")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполонен успешно, должность создана",
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
    ResponseEntity<Void> savePosition(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PositionDTO.class)
                    )
            )
            @RequestBody PositionDTO dto) {
        directoryService.savePosition(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить должность")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно, должность обновлена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Должность с указанным ID не найдена",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    void updatePosition(
            @Parameter(description = "Идентификатор должности")
            @PathVariable(name = "id") Long positionId,
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PositionDTO.class)
                    )
            )
            @RequestBody PositionDTO positionDTO) {
        directoryService.updatePosition(positionId, positionDTO);
    }

    @Operation(
            summary = "Лучший сотрудник по позиции",
            description = "Возвращает сотрудника, продавшего наиболшее количество товаров." +
                    "При переданном параметре itemId учитываются только продажи конкретного товара."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BestEmployeeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдена должность или для заданной должности нет продаж",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("/{positionId}/employees/best")
    BestEmployeeDTO getBestPosition(
            @Parameter(description = "Идентификатор должности", example = "2")
            @PathVariable Long positionId,
            @Parameter(description = "Идентификатор типа товара", example = "5")
            @RequestParam(required = false) Long itemTypeId) {
        if (itemTypeId == null)
            return employeeService.getBestEmployee(positionId);
        else
            return employeeService.getBestEmployeeWithConcreteItem(positionId, itemTypeId);
    }
}
