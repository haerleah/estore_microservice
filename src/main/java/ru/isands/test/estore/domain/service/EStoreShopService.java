package ru.isands.test.estore.domain.service;

import ru.isands.test.estore.dto.AvailabilityDTO;
import ru.isands.test.estore.dto.PaymentSummaryDTO;
import ru.isands.test.estore.exception.ResourceNotFoundException;

/**
 * Сервис для работы с магазинами
 */
public interface EStoreShopService {
    AvailabilityDTO getAvailability(Long shopId, Long electroItemId)
            throws ResourceNotFoundException;

    PaymentSummaryDTO getPaymentSummary(Long shopId, Long purchaseTypeId)
            throws ResourceNotFoundException;
}
