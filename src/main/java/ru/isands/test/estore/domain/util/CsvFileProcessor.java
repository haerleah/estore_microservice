package ru.isands.test.estore.domain.util;

import ru.isands.test.estore.domain.util.processors.priority.Priority;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Интерфейс для обработки CSV-файлов, полученных из ZIP-архива.
 * <p>
 * Каждая реализация отвечает за:
 *     <ul>
 *         <li>определение по имени, какие файлы он может обрабатывать,</li>
 *         <li>чтение и парсинг содержимого CSV-файла,</li>
 *         <li>сохранение и передача данных в соответсвующий сервис.</li>
 *     </ul>
 * </p>
 */
public interface CsvFileProcessor {
    /**
     * Возвращает приортет процессора, чем меньше приоритет, тем раньше отработает процессор,
     * относительно остальных.
     * @return Приоритет процессора
     * @see Priority
     */
    Priority getPriority();

    /**
     * Возвращает имя CSV-файла, который этот процессор может обработать.
     *
     * @return Имя файла, которое может быть обработано.
     */
    String getFileName();

    /**
     * Обрабатывает содержимое CSV-файла:
     * <ul>
     *        <li>читает все записи из {@code csvStream},</li>
     *        <li>преобразовывает их в сущности,</li>
     *        <li>передает преобразованные сущности в соответсвующий сервис, для сохранения.</li>
     *    </ul>
     *
     * @param csvStream поток входных данных CSV-файла, не должен быть {@code null}.
     */
    void process(InputStream csvStream) throws UnsupportedEncodingException;
}
