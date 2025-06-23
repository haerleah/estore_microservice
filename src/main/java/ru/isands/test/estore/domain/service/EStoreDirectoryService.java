package ru.isands.test.estore.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.isands.test.estore.dto.PositionDTO;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.dto.ShopDTO;
import ru.isands.test.estore.dto.PurchaseTypeDTO;

/**
 * Сервис для работы со справочниками.
 */
public interface EStoreDirectoryService {
    Page<PositionDTO> getPositions(Pageable pageable);
    Page<ElectroTypeDTO> getElectroTypes(Pageable pageable);
    Page<ShopDTO> getShops(Pageable pageable);
    Page<PurchaseTypeDTO> getPurchaseTypes(Pageable pageable);
    void savePosition(PositionDTO positionDTO);
    void saveElectroType(ElectroTypeDTO electroTypeDTO);
    void saveShop(ShopDTO shopDTO);
    void savePurchaseType(PurchaseTypeDTO purchaseTypeDTO);
    void updateShop(Long shopId, ShopDTO shopDTO);
    void updatePurchaseType(Long purchaseTypeId, PurchaseTypeDTO purchaseTypeDTO);
    void updateElectroType(Long electroTypeId, ElectroTypeDTO electroTypeDTO);
    void updatePosition(Long positionId, PositionDTO positionDTO);
}
