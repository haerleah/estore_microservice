package ru.isands.test.estore.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.isands.test.estore.dao.entity.Purchase;
import ru.isands.test.estore.dto.PurchaseDTO;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("select new ru.isands.test.estore.dto.PurchaseDTO(" +
            "pu.id, ei.id as electroItemId, ei.name as electroItemName, e.id as employeeId, " +
            "concat(e.lastName, ' ', e.firstName, ' ', e.patronymic) as employeeName, " +
            "pu.purchaseDate, s.id as shopId, s.name as shopName, pt.id as purchaseTypeId, pt.name as purchaseType) " +
            "from Purchase pu " +
            "join ElectroItem ei on ei.id = pu.electroId " +
            "join Employee e on e.id = pu.employeeId " +
            "join Shop s on s.id = pu.shopId " +
            "join PurchaseType pt on pt.id = pu.type")
    Page<PurchaseDTO> findAllWithOffsetAndLimit(Pageable pageable);
}
