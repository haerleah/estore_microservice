package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
import ru.isands.test.estore.dao.repo.ElectroShopRepository;
import ru.isands.test.estore.domain.mapper.EStoreMapper;
import ru.isands.test.estore.dto.ElectroItemCreateDTO;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.dto.ElectroItemPutDTO;
import ru.isands.test.estore.dto.StockDTO;
import ru.isands.test.estore.exception.ResourceNotFoundException;

/**
 * Стандартная реализация {@link EStoreElectroItemService}, работающая
 * через JPA-репозиторий.
 */
@RequiredArgsConstructor
@Service
public class EStoreElectroItemServiceImplementation implements EStoreElectroItemService {
    private final ElectroItemRepository electroItemRepository;
    private final ElectroShopRepository electroShopRepository;
    private final EStoreMapper eStoreMapper;

    @Override
    public Page<ElectroItemDTO> getElectroItems(Pageable pageable) {
        return electroItemRepository.findAllWithOffsetAndLimit(pageable);
    }

    @Override
    @Transactional
    public void saveElectroItem(ElectroItemCreateDTO electroItemDTO) {
        ElectroItem item = electroItemRepository.save(eStoreMapper.toEntity(electroItemDTO));
        electroShopRepository.saveAll(eStoreMapper.toEntityFromStocks(electroItemDTO.getStocks(), item.getId()));
    }

    @Override
    @Transactional
    public void updateElectroItem(Long itemId, ElectroItemPutDTO electroItemPutDTO) {
        ElectroItem item = electroItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        long count = electroItemPutDTO.getStocks().stream().mapToLong(StockDTO::getCount).sum();
        ElectroItemDTO mainData = electroItemPutDTO.getItem();
        item.setCount(count);
        item.setArchive(mainData.isArchive());
        item.setDescription(mainData.getDescription());
        item.setPrice(mainData.getPrice());
        item.setName(mainData.getName());
        item.setTypeId(mainData.getElectroTypeId());
        electroItemRepository.save(item);
        electroShopRepository.saveAll(eStoreMapper.toEntityFromStocks(electroItemPutDTO.getStocks(), item.getId()));
    }
}
