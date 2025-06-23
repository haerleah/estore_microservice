package ru.isands.test.estore.domain.util.processors.priority;

/**
 * Уровни приоритета для порядка загрузки данных из CSV/ZIP.
 * <p>Обеспечивают правильную последовательность вставки в базу с учётом внешних ключей.</p>
 *
 * <ul>
 *   <li>{@code REFERENCE}     — загрузка справочников (reference data).</li>
 *   <li>{@code MASTER}        — основные сущности (master data).</li>
 *   <li>{@code JUNCTION}      — таблицы-связки (association/junction data).</li>
 *   <li>{@code TRANSACTIONAL} — данные событий/операций (transactional data).</li>
 * </ul>
 */
public enum Priority {
    REFERENCE,
    MASTER,
    JUNCTION,
    TRANSACTIONAL,
}
