package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.domain.service.ZipReaderService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Data", description = "Сервис для загрузки данных из ZIP-архива CSV-файлов")
@RequestMapping("/estore/api/import")
public class DataImportController {
    private final ZipReaderService zipReaderService;

    @Operation(
            summary = "Импортировать данные",
            description =
                    "Принимает ZIP-архив, содержащий CSV-файлы для разных сущностей:\n " +
                            "- Employee.csv,\n - ElectroShop.csv,\n - Purchase.csv и т.д.\n " +
                            "Записи из файлов сохраняются в базе данных."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно считаны и сохранены"),
            @ApiResponse(responseCode = "400", description = "Архив пуст, либо нет ни одного корректного CSV-файла"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping(path = "/all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importAll(
            @Parameter(
                    description = "ZIP-архив с CSV-файлами для каждой сущности",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(name = "file") MultipartFile zipFile) throws IOException {
        zipReaderService.readZipFileAndSave(zipFile.getBytes());
    }
}
