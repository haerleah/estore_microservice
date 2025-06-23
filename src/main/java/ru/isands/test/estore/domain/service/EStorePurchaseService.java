package ru.isands.test.estore.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.isands.test.estore.dto.PurchaseCommitDTO;
import ru.isands.test.estore.dto.PurchaseDTO;

/**
 * Сервис для работы с покупками
 */
public interface EStorePurchaseService {
    Page<PurchaseDTO> getPurchases(Pageable pageable);
    void savePurchase(PurchaseCommitDTO purchaseDTO);
    void updatePurchase(Long purchaseId, PurchaseCommitDTO purchaseDTO);
}
