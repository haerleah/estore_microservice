package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.Purchase;
import ru.isands.test.estore.dao.repo.*;
import ru.isands.test.estore.domain.mapper.EStoreMapper;
import ru.isands.test.estore.dto.PurchaseCommitDTO;
import ru.isands.test.estore.dto.PurchaseDTO;
import ru.isands.test.estore.exception.PurchaseConflictException;
import ru.isands.test.estore.exception.ResourceNotFoundException;


/**
 * Стандартная реализация {@link EStorePurchaseService}, работающая
 * через JPA-репозитории.
 */
@RequiredArgsConstructor
@Service
public class EStorePurchaseServiceImplementation implements EStorePurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final EmployeeRepository employeeRepository;
    private final ElectroShopRepository electroShopRepository;
    private final ElectroEmployeeRepository electroEmployeeRepository;
    private final ElectroItemRepository electroItemRepository;
    private final EStoreMapper eStoreMapper;

    @Override
    public Page<PurchaseDTO> getPurchases(Pageable pageable) {
        return purchaseRepository.findAllWithOffsetAndLimit(pageable);
    }

    @Override
    @Transactional
    public void savePurchase(PurchaseCommitDTO purchaseDTO) {
        ElectroItem item = findItemOrThrow(purchaseDTO.getElectroItemId());
        ElectroShop shop = findShopOrThrow(purchaseDTO.getShopId(), purchaseDTO.getElectroItemId());
        validateShopHasStock(shop);
        validateEmployeeCanSell(purchaseDTO.getEmployeeId(), purchaseDTO.getShopId(), item.getTypeId());

        Purchase purchase = eStoreMapper.toEntity(purchaseDTO);
        purchaseRepository.save(purchase);

        decrementStock(item, shop);
    }

    @Override
    @Transactional
    public void updatePurchase(Long purchaseId, PurchaseCommitDTO purchaseDTO) {
        Purchase previous = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));
        ElectroItem item = findItemOrThrow(previous.getElectroId());
        ElectroShop shop = findShopOrThrow(previous.getShopId(), previous.getElectroId());
        incrementStock(item, shop);

        item = findItemOrThrow(purchaseDTO.getElectroItemId());
        shop = findShopOrThrow(purchaseDTO.getShopId(), purchaseDTO.getElectroItemId());
        validateShopHasStock(shop);
        validateEmployeeCanSell(purchaseDTO.getEmployeeId(), purchaseDTO.getShopId(), item.getTypeId());

        Purchase purchase = eStoreMapper.toEntity(purchaseDTO);
        purchaseRepository.save(purchase);

        decrementStock(item, shop);
    }

    private ElectroItem findItemOrThrow(Long itemId) {
        return electroItemRepository.findById(itemId)
                .orElseThrow(() -> new PurchaseConflictException(
                        String.format("Item with id=%d not found", itemId)));
    }

    private ElectroShop findShopOrThrow(Long shopId, Long itemId) {
        return electroShopRepository
                .findByShopIdAndElectroItemId(shopId, itemId)
                .orElseThrow(() -> new PurchaseConflictException(
                        String.format("Item id=%d is not available in shop id=%d", itemId, shopId)));
    }

    private void validateShopHasStock(ElectroShop shop) {
        if (shop.getCount() <= 0) {
            throw new PurchaseConflictException(
                    String.format("Shop id=%d has no stock for item id=%d",
                            shop.getShopId(), shop.getElectroItemId()));
        }
    }

    private void validateEmployeeCanSell(Long employeeId, Long shopId, Long itemTypeId) {
        boolean worksInShop = employeeRepository
                .existsByIdAndShopId(employeeId, shopId);
        if (!worksInShop) {
            throw new PurchaseConflictException(
                    String.format("Employee id=%d does not work in shop id=%d",
                            employeeId, shopId));
        }

        boolean authorized = electroEmployeeRepository
                .existsByEmployeeIdAndElectroTypeId(employeeId, itemTypeId);
        if (!authorized) {
            throw new PurchaseConflictException(
                    String.format("Employee id=%d is not authorized to sell item type id=%d",
                            employeeId, itemTypeId));
        }
    }

    private void decrementStock(ElectroItem item, ElectroShop shop) {
        item.setCount(item.getCount() - 1);
        if(item.getCount() <= 0)
            item.setArchive(true);
        electroItemRepository.save(item);

        shop.setCount(shop.getCount() - 1);
        electroShopRepository.save(shop);
    }

    private void incrementStock(ElectroItem item, ElectroShop shop) {
        item.setCount(item.getCount() + 1);
        if(item.getCount() >= 1)
            item.setArchive(false);
        electroItemRepository.save(item);

        shop.setCount(shop.getCount() + 1);
        electroShopRepository.save(shop);
    }
}
