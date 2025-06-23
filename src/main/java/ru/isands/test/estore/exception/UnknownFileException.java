package ru.isands.test.estore.exception;

/**
 * Сигнализирует о наличии в архиве файла с незарегистрированным именем
 */
public class UnknownFileException extends RuntimeException {
    public UnknownFileException(String message) {
        super(message);
    }
}
