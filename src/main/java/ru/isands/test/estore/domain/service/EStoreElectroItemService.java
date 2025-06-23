package ru.isands.test.estore.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.isands.test.estore.dto.ElectroItemCreateDTO;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.dto.ElectroItemPutDTO;

/**
 * Сервис для работы с товарами
 */
public interface EStoreElectroItemService {
    Page<ElectroItemDTO> getElectroItems(Pageable pageable);
    void saveElectroItem(ElectroItemCreateDTO electroItemDTO);
    void updateElectroItem(Long itemId, ElectroItemPutDTO electroItemPutDTO);
}
