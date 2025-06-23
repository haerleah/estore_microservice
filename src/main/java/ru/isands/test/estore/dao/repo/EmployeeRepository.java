package ru.isands.test.estore.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dto.BestEmployeeDTO;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.dto.EmployeePerformanceDTO;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select new ru.isands.test.estore.dto.EmployeeDTO(" +
            "e.id, e.firstName, e.lastName, e.patronymic, e.birthDate, p.id as positionId, " +
            "p.name as position, s.id as shopId, s.name as shopName, e.gender) " +
            "from Employee e " +
            "join PositionType p on e.positionId = p.id " +
            "join Shop s on e.shopId = s.id ")
    Page<EmployeeDTO> findAllWithOffsetAndLimit(Pageable pageable);

    @Query("select new ru.isands.test.estore.dto.EmployeePerformanceDTO(\n" +
            "e.id, concat(e.lastName, ' ', e.firstName, ' ', e.patronymic),\n" +
            "sum(ei.price) as soldSum, count(p.id) as soldCount) " +
            "from Purchase p\n " +
            "join Employee e on e.id = p.employeeId\n " +
            "join ElectroItem ei on ei.id = p.electroId\n " +
            "where e.positionId = :positionId and p.purchaseDate between :from and :to\n " +
            "group by e.id")
    List<EmployeePerformanceDTO> findLastYearBestEmployees(long positionId, Date from, Date to, Pageable pageable);

    @Query("select new ru.isands.test.estore.dto.BestEmployeeDTO(" +
            "e.id, concat(e.lastName, ' ', e.firstName, ' ', e.patronymic), " +
            "pt.name as positionName, " +
            "s.name as shopName, count(pu) as soldCount) " +
            "from Employee e " +
            "join PositionType pt on e.positionId = pt.id " +
            "join Shop s on e.shopId = s.id " +
            "join Purchase pu on pu.employeeId = e.id " +
            "where pt.id = :positionId " +
            "group by pt.id, e.id, s.id " +
            "order by count(pu) desc")
    List<BestEmployeeDTO> getBestEmployee(Long positionId, Pageable pageable);

    @Query("select new ru.isands.test.estore.dto.BestEmployeeDTO(" +
            "e.id, concat(e.lastName, ' ', e.firstName, ' ', e.patronymic), " +
            "pt.name as positionName, " +
            "s.name as shopName, count(pu) as soldCount) " +
            "from Employee e " +
            "join PositionType pt on e.positionId = pt.id " +
            "join Shop s on e.shopId = s.id " +
            "join Purchase pu on pu.employeeId = e.id " +
            "join ElectroItem ei on ei.id = pu.electroId " +
            "where pt.id = :positionId and ei.typeId = :itemTypeId " +
            "group by pt.id, e.id, s.id " +
            "order by count(pu) desc ")
    List<BestEmployeeDTO> getBestEmployeeWithItemType(Long positionId, Long itemTypeId, Pageable pageable);

    boolean existsByIdAndShopId(Long id, Long shopId);
}