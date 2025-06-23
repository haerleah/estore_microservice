package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.dao.entity.ElectroEmployee;

public interface ElectroEmployeeRepository extends JpaRepository<ElectroEmployee, Long> {
    boolean existsByEmployeeIdAndElectroTypeId(Long employeeId, Long electroTypeId);
}
