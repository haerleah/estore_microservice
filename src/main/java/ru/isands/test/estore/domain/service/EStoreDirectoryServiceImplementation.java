package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dao.entity.ElectroType;
import ru.isands.test.estore.dao.entity.PositionType;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.repo.*;
import ru.isands.test.estore.domain.mapper.EStoreMapper;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.dto.PositionDTO;
import ru.isands.test.estore.dto.PurchaseTypeDTO;
import ru.isands.test.estore.dto.ShopDTO;

/**
 * Стандартная реализация {@link EStoreDirectoryService}, работающая
 * через JPA-репозитории.
 */
@RequiredArgsConstructor
@Service
public class EStoreDirectoryServiceImplementation implements EStoreDirectoryService {
    private final PositionRepository positionRepository;
    private final ElectroTypeRepository electroTypeRepository;
    private final ShopRepository shopRepository;
    private final PurchaseTypeRepository purchaseTypeRepository;
    private final EStoreMapper eStoreMapper;

    @Override
    public Page<PositionDTO> getPositions(Pageable pageable) {
        Page<PositionType> positionTypes = positionRepository.findAll(pageable);
        return positionTypes.map(eStoreMapper::toDto);
    }

    @Override
    public Page<ElectroTypeDTO> getElectroTypes(Pageable pageable) {
        Page<ElectroType> electroTypes = electroTypeRepository.findAll(pageable);
        return electroTypes.map(eStoreMapper::toDto);
    }

    @Override
    public Page<ShopDTO> getShops(Pageable pageable) {
        Page<Shop> shops = shopRepository.findAll(pageable);
        return shops.map(eStoreMapper::toDto);
    }

    @Override
    public Page<PurchaseTypeDTO> getPurchaseTypes(Pageable pageable) {
        Page<PurchaseType> purchaseTypes = purchaseTypeRepository.findAll(pageable);
        return purchaseTypes.map(eStoreMapper::toDto);
    }

    @Override
    public void savePosition(PositionDTO positionDTO) {
        positionRepository.save(eStoreMapper.toEntity(positionDTO));
    }

    @Override
    public void saveElectroType(ElectroTypeDTO electroTypeDTO) {
        electroTypeRepository.save(eStoreMapper.toEntity(electroTypeDTO));
    }

    @Override
    public void saveShop(ShopDTO shopDTO) {
        shopRepository.save(eStoreMapper.toEntity(shopDTO));
    }

    @Override
    public void savePurchaseType(PurchaseTypeDTO purchaseTypeDTO) {
        purchaseTypeRepository.save(eStoreMapper.toEntity(purchaseTypeDTO));
    }

    @Override
    public void updateShop(Long shopId, ShopDTO shopDTO) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new RuntimeException("Shop not found"));
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
        shopRepository.save(shop);
    }

    @Override
    public void updatePurchaseType(Long purchaseTypeId, PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType purchaseType = purchaseTypeRepository.findById(purchaseTypeId)
                .orElseThrow(() -> new RuntimeException("Purchase type not found"));
        purchaseType.setName(purchaseTypeDTO.getName());
        purchaseTypeRepository.save(purchaseType);
    }

    @Override
    public void updateElectroType(Long electroTypeId, ElectroTypeDTO electroTypeDTO) {
        ElectroType electroType = electroTypeRepository.findById(electroTypeId)
                .orElseThrow(() -> new RuntimeException("Electro item type not found"));
        electroType.setName(electroTypeDTO.getName());
        electroTypeRepository.save(electroType);
    }

    @Override
    public void updatePosition(Long positionId, PositionDTO positionDTO) {
        PositionType positionType = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position type not found"));
        positionType.setName(positionDTO.getName());
        positionRepository.save(positionType);
    }
}
