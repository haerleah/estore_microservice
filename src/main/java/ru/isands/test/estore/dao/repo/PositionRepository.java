package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.isands.test.estore.dao.entity.PositionType;

public interface PositionRepository extends JpaRepository<PositionType, Long> {
    @Query("select p.name from PositionType p where p.id = :positionTypeId")
    String getPositionNameById(Long positionTypeId);
}
