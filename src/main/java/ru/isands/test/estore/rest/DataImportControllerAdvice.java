package ru.isands.test.estore.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.exception.CsvParseException;
import ru.isands.test.estore.exception.MissingFileException;
import ru.isands.test.estore.exception.UnknownFileException;

import java.util.zip.ZipException;

/**
 * Специфичный обработчик ошибок для контроллера загрузки данных.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = DataImportController.class)
public class DataImportControllerAdvice {

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorDTO> handle(InvalidDataAccessApiUsageException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(422,
                "CSV data does not match database model"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({MissingFileException.class, UnknownFileException.class, CsvParseException.class})
    public ResponseEntity<ErrorDTO> handle(Exception e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZipException.class)
    public ResponseEntity<ErrorDTO> handle(ZipException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(400, "Invalid ZIP-archive"), HttpStatus.BAD_REQUEST);
    }
}
