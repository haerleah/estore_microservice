package ru.isands.test.estore.domain.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.exception.CsvParseException;
import ru.isands.test.estore.exception.MissingFileException;
import ru.isands.test.estore.exception.UnknownFileException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Диспетчер, обеспечивающий выбор корректного {@link CsvFileProcessor} для каждого файла.
 * Основной критерий для выбора процессора - имя файла.
 */
@Getter
@Slf4j
@Service
public class CsvImportDispatcher {
    private final List<CsvFileProcessor> processors;

    public CsvImportDispatcher(Set<CsvFileProcessor> processors) {
        this.processors = processors.stream()
                .sorted(Comparator.comparing(CsvFileProcessor::getPriority))
                .collect(Collectors.toList());
    }

    /**
     * Определяет возможность обработать файл, сверяя его имя с доступными именами у процессоров,
     * читает его содержимое и сохраняет полученные данные в БД.
     *
     * @param zipData содержимое ZIP-архива в парах: имя файла - содержимое
     * @throws CsvParseException                  в случае неверной кодировки CSV-файла.
     * @throws InvalidDataAccessApiUsageException в случае, если содержимое файла не соответсвует сущности в БД.
     * @throws MissingFileException               в случае, если архив содержит не полный набор файлов.
     */
    public void dispatch(Map<String, byte[]> zipData) throws CsvParseException, UnknownFileException {
        for (CsvFileProcessor processor : processors) {
            String fileName = processor.getFileName();
            byte[] fileData = zipData.get(fileName);
            if (fileData == null) throw new MissingFileException(fileName + " not found");
            try {
                InputStream fileStream = new ByteArrayInputStream(fileData);
                processor.process(fileStream);
            } catch (UnsupportedEncodingException e) {
                throw new CsvParseException("Invalid encoding for file " + fileName);
            }
        }
    }
}
