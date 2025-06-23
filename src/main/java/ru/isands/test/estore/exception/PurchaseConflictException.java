package ru.isands.test.estore.exception;

/**
 * Бросается, в случае ошибок при создании покупки
 */
public class PurchaseConflictException extends RuntimeException {
    public PurchaseConflictException(String message) {
        super(message);
    }
}
