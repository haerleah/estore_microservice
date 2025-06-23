package ru.isands.test.estore.domain.mapper;

import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.*;
import ru.isands.test.estore.dto.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Маппер, обеспечивающий конвертацию сущностей БД в DTO
 */
@Component
public class EStoreMapper {
    public PositionDTO toDto(PositionType positionType) {
        return PositionDTO.builder()
                .id(positionType.getId())
                .name(positionType.getName())
                .build();
    }

    public ElectroTypeDTO toDto(ElectroType electroType) {
        return ElectroTypeDTO.builder()
                .id(electroType.getId())
                .name(electroType.getName())
                .build();
    }

    public ShopDTO toDto(Shop shop) {
        return ShopDTO.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .build();
    }

    public PurchaseTypeDTO toDto(PurchaseType purchaseType) {
        return PurchaseTypeDTO.builder()
                .id(purchaseType.getId())
                .name(purchaseType.getName())
                .build();
    }

    public ElectroItem toEntity(ElectroItemCreateDTO electroItemDTO) {
        ElectroItem electroItem = new ElectroItem();
        electroItem.setTypeId(electroItemDTO.getElectroTypeId());
        electroItem.setName(electroItemDTO.getName());
        electroItem.setDescription(electroItemDTO.getDescription());
        electroItem.setPrice(electroItemDTO.getPrice());
        electroItem.setCount(electroItemDTO.getStocks().stream().mapToLong(StockDTO::getCount).sum());
        electroItem.setArchive(electroItemDTO.getArchive());
        return electroItem;
    }

    public Employee toEntity(EmployeeCreateDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPatronymic(employeeDTO.getPatronymic());
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setGender(employeeDTO.getGender());
        employee.setPositionId(employeeDTO.getPositionId());
        employee.setShopId(employeeDTO.getShopId());
        return employee;
    }

    public List<ElectroEmployee> toEntityFromIds(List<Long> availableElectroTypeIds, Long employeeId) {
        return availableElectroTypeIds.stream().map(id -> {
            ElectroEmployee electroEmployee = new ElectroEmployee();
            electroEmployee.setElectroTypeId(id);
            electroEmployee.setEmployeeId(employeeId);
            return electroEmployee;
        }).collect(Collectors.toList());
    }

    public List<ElectroShop> toEntityFromStocks(List<StockDTO> stockDTO, Long electroItemId) {
        return stockDTO.stream().map(dto -> {
            ElectroShop electroShop = new ElectroShop();
            electroShop.setShopId(dto.getShopId());
            electroShop.setElectroItemId(electroItemId);
            electroShop.setCount(dto.getCount());
            return electroShop;
        }).collect(Collectors.toList());
    }

    public PurchaseType toEntity(PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType purchaseType = new PurchaseType();
        purchaseType.setName(purchaseTypeDTO.getName());
        return purchaseType;
    }

    public ElectroType toEntity(ElectroTypeDTO electroTypeDTO) {
        ElectroType electroType = new ElectroType();
        electroType.setName(electroTypeDTO.getName());
        return electroType;
    }

    public Shop toEntity(ShopDTO shopDTO) {
        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
        return shop;
    }

    public PositionType toEntity(PositionDTO positionDTO) {
        PositionType positionType = new PositionType();
        positionType.setName(positionDTO.getName());
        return positionType;
    }

    public Purchase toEntity(PurchaseCommitDTO purchaseDTO) {
        Purchase purchase = new Purchase();
        purchase.setEmployeeId(purchaseDTO.getEmployeeId());
        purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
        purchase.setElectroId(purchaseDTO.getElectroItemId());
        purchase.setShopId(purchaseDTO.getShopId());
        purchase.setType(purchaseDTO.getPurchaseTypeId());
        return purchase;
    }
}
