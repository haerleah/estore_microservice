package ru.isands.test.estore.exception;

import ru.isands.test.estore.rest.GlobalControllerAdvice;

/**
 * Бросается, в случае, если ресурс не найден в БД.
 * <p>Исключение перехватывается в {@link GlobalControllerAdvice#handle(ResourceNotFoundException)},
 * который вернет HTTP 404 клиенту.</p>
 * @see GlobalControllerAdvice
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
