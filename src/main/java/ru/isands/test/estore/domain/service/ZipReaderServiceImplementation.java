package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isands.test.estore.domain.util.CsvImportDispatcher;
import ru.isands.test.estore.exception.CsvParseException;
import ru.isands.test.estore.exception.MissingFileException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Стандартная реализация {@link ZipReaderService}.
 * Читает файлы из архива и в зависимости от имени файла, сохраняет данные в определенную таблицу.
 * Взаимодействие с БД происходит через JPA-репозитории.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ZipReaderServiceImplementation implements ZipReaderService {
    private final CsvImportDispatcher csvImportDispatcher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readZipFileAndSave(byte[] data) throws IOException, CsvParseException, MissingFileException {
        if (data == null || data.length == 0) throw new IllegalArgumentException("Data is null or empty");

        Map<String, byte[]> zipData = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                zipData.put(entry.getName(), zis.readAllBytes());
                zis.closeEntry();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }

        csvImportDispatcher.dispatch(zipData);
    }
}
