package ru.isands.test.estore.exception;

/**
 * Выбрасывается, если архив содержит неполный набор файлов
 */
public class MissingFileException extends RuntimeException {
    public MissingFileException(String message) {
        super(message);
    }
}
