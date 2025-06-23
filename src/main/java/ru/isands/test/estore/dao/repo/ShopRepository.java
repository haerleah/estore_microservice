package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dto.PaymentSummaryDTO;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("select new ru.isands.test.estore.dto.PaymentSummaryDTO(" +
            "s.id, pt.id, s.name, pt.name,  coalesce(sum(ei.price), 0)  as totalAmount) " +
            "from Purchase p " +
            "join Shop s on s.id = p.shopId " +
            "join PurchaseType pt on pt.id = p.type " +
            "join ElectroItem ei on ei.id = p.electroId " +
            "where p.shopId = :shopId and pt.id = :purchaseTypeId " +
            "group by s.id, pt.id")
    PaymentSummaryDTO getPaymentSummaryDTO(Long shopId, Long purchaseTypeId);
}
