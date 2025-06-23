package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.isands.test.estore.domain.service.EStoreEmployeeService;
import ru.isands.test.estore.dto.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {
    private final EStoreEmployeeService employeeService;

    @Operation(
            summary = "Получить список сотрудников (опциональная пагинация и сортировка)",
            description = "Возвращает список сотрудников. Доступные параметры пагинации и сортировки:\n " +
                    "- `page` - индекс страницы (>= 0)\n " +
                    "- `size` - количество записей на страницу (>= 1)\n " +
                    "- `sort` - поле и направление сортировки, например `lastName, desc`\n " +
                    "Сортировка возможна по следующим полям: `id`, `firstName`, `lastName`, " +
                    "`patronymic`, `birthDate`, `positionName`, `shopName`, `gender`"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EmployeeDTO.class)
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
    Page<EmployeeDTO> getEmployees(
            @ParameterObject
            @PageableDefault(size = 1000, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return employeeService.getEmployees(pageable);
    }

    @Operation(summary = "Сохранить сотрудника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполонен успешно, сотрудник создан",
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
    ResponseEntity<Void> saveEmployee(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeCreateDTO.class)
                    )
            )
            @RequestBody EmployeeCreateDTO employeeCreateDTO) {
        employeeService.saveEmployee(employeeCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить сотрудника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно, сотрудник обновлен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат JSON или ошибка валидации",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден сотрудник с указанным ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    void updateEmployee(
            @Parameter(description = "Идентификатор сотрудника")
            @PathVariable(name = "id") Long employeeId,
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeePutDTO.class)
                    )
            )
            @RequestBody EmployeePutDTO employeeDTO) {
        employeeService.updateEmployee(employeeId, employeeDTO);
    }

    @Operation(
            summary = "Список лучших сотрудников за последний год",
            description = "Возвращает список сотрудников, занявших первые позиции по заданным метрикам " +
                    "за период между текущей датой и датой 365 дней назад. " +
                    "Если метрика не указана, возвращается по количеству продаж. " +
                    "Приоритет сортировки зависит от порядка переданных параметров.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполонен успешно",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EmployeePerformanceDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
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
    @GetMapping("/best")
    List<EmployeePerformanceDTO> getLastYearBestEmployees(
            @Parameter(
                    description = "Идентификатор должности",
                    example = "5",
                    required = true
            )
            @RequestParam Long positionId,
            @Parameter(
                    description = "За сколько лет проводить выборку",
                    example = "2",
                    required = true
            )
            @RequestParam Integer year,
            @Parameter(
                    description = "Метрики для ранжирования: количество продаж (soldCount) и/или сумма продаж (soldSum)",
                    required = true,
                    schema = @Schema(
                            type = "array",
                            implementation = Metric.class,
                            example = "[\"soldCount\",\"soldSum\"]"
                    )
            )
            @RequestParam List<Metric> metrics,
            @Parameter(
                    description = "Максимальное число возвращаемых записей",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int limit
    ) {
        return employeeService.getLastYearBestEmployees(positionId, metrics, limit, year);
    }

}
