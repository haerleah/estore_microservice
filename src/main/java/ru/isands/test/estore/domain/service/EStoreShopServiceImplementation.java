package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.repo.ElectroShopRepository;
import ru.isands.test.estore.dao.repo.PurchaseTypeRepository;
import ru.isands.test.estore.dao.repo.ShopRepository;
import ru.isands.test.estore.dto.AvailabilityDTO;
import ru.isands.test.estore.dto.PaymentSummaryDTO;
import ru.isands.test.estore.exception.ResourceNotFoundException;

/**
 * Стандартная реализация {@link EStoreShopService}, работающая
 * через JPA-репозиторий.
 */
@RequiredArgsConstructor
@Service
public class EStoreShopServiceImplementation implements EStoreShopService {
    private final ElectroShopRepository electroShopRepository;
    private final PurchaseTypeRepository purchaseTypeRepository;
    private final ShopRepository shopRepository;

    /**
     * Находит остаток товара с {@code electroItemId} в магазине с {@code shopId}.
     *
     * @param shopId        идентификатор магазина.
     * @param electroItemId идентификатор товара.
     * @return Остаток товара в магазине, в виде {@link AvailabilityDTO}.
     * @throws ResourceNotFoundException в случае, если нет магазина и/или товара с указанными идентификаторами.
     */
    @Override
    public AvailabilityDTO getAvailability(Long shopId, Long electroItemId) throws ResourceNotFoundException {
        ElectroShop electroShop = electroShopRepository.findByShopIdAndElectroItemId(shopId, electroItemId)
                .orElseThrow(() -> new ResourceNotFoundException("No such item with given shopId and electroItemId"));
        return new AvailabilityDTO(electroShop.getCount());
    }

    /**
     * Находит сумму продаж в магазине с {@code shopId}, со способом оплаты {@code purchaseTypeId}.
     *
     * @param shopId         идентификатор магазина.
     * @param purchaseTypeId идентификатор способа оплаты.
     * @return Сумма проданных товаров, в виде {@link PaymentSummaryDTO}.
     * @throws ResourceNotFoundException в случае, если нет магазина и/или способа оплаты с указанными идентификаторами.
     */
    @Override
    public PaymentSummaryDTO getPaymentSummary(Long shopId, Long purchaseTypeId) throws ResourceNotFoundException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("No such shop with id: " + shopId));
        PurchaseType purchaseType = purchaseTypeRepository.findById(purchaseTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("No such purchase type with id: " + purchaseTypeId));
        PaymentSummaryDTO summary = shopRepository.getPaymentSummaryDTO(shopId, purchaseTypeId);
        if (summary == null) {
            summary = new PaymentSummaryDTO(shopId, purchaseTypeId, shop.getName(), purchaseType.getName(), 0L);
        }
        return summary;
    }
}
