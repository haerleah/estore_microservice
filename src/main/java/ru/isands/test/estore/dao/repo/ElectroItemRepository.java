package ru.isands.test.estore.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dto.ElectroItemDTO;

public interface ElectroItemRepository extends JpaRepository<ElectroItem, Long> {
    @Query("select new ru.isands.test.estore.dto.ElectroItemDTO(" +
            "ei.id, ei.name, et.id as typeId, et.name as type, ei.price, ei.count, ei.archive, ei.description) " +
            "from ElectroItem ei " +
            "join ElectroType et on et.id = ei.typeId ")
    Page<ElectroItemDTO> findAllWithOffsetAndLimit(Pageable pageable);
}
