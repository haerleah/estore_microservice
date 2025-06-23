package ru.isands.test.estore.exception;

/**
 * Сигнализирует об недопустимой кодировке CSV-файла
 */
public class CsvParseException extends RuntimeException {
    public CsvParseException(String message) {
        super(message);
    }
}
