package ru.isands.test.estore.domain.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.io.IOException;
import java.util.zip.ZipException;

import ru.isands.test.estore.exception.CsvParseException;
import ru.isands.test.estore.exception.MissingFileException;

/**
 * Сервис для чтения данных из ZIP-архива и сохранения их в базу данных.
 */
public interface ZipReaderService {
    /**
     * Считывает содержимое переданного ZIP-архива и сохраняет извлечённые данные в базу данных.
     *
     * <p>Ожидается, что ZIP-архив содержит CSV-файлы, соответствующие определённой таблице в БД.
     * Файлы, имя которых, не соотвествует ни одной сущности - игнорируются.
     * Все записи из CSV-файлов будут импортированы транзакционно.</p>
     *
     * @param data байтовый массив с содержимым ZIP-архива; должен быть в
     *             корректном ZIP-формате и не быть {@code null}
     * @throws IllegalArgumentException           если {@code data} пуста или равна {@code null}
     * @throws ZipException                       если содержимое не распознано как ZIP-архив
     * @throws MissingFileException               если архив содержит не полный набор файлов
     * @throws CsvParseException                  если содержимое CSV-файла имеет кодировку, отличную от {@code windows-1251}
     * @throws InvalidDataAccessApiUsageException если данные из CSV не соответствуют модели БД
     * @throws IOException                        при прочих чтения или записи
     */
    void readZipFileAndSave(byte[] data) throws IOException, CsvParseException;
}
