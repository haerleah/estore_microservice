package ru.isands.test.estore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.entity.PositionType;
import ru.isands.test.estore.dao.repo.ElectroEmployeeRepository;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
import ru.isands.test.estore.dao.repo.EmployeeRepository;
import ru.isands.test.estore.dao.repo.PositionRepository;
import ru.isands.test.estore.domain.mapper.EStoreMapper;
import ru.isands.test.estore.dto.*;
import ru.isands.test.estore.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Стандартная реализация {@link EStoreEmployeeService}, работающая
 * через JPA-репозиторий.
 */
@Service
@RequiredArgsConstructor
public class EStoreEmployeeServiceImplementation implements EStoreEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ElectroEmployeeRepository electroEmployeeRepository;
    private final PositionRepository positionRepository;
    private final ElectroItemRepository electroItemRepository;
    private final EStoreMapper eStoreMapper;

    public Page<EmployeeDTO> getEmployees(Pageable pageable) {
        return employeeRepository.findAllWithOffsetAndLimit(pageable);
    }

    @Override
    @Transactional
    public void saveEmployee(EmployeeCreateDTO employeeDTO) {
        Employee employee = employeeRepository.save(eStoreMapper.toEntity(employeeDTO));
        electroEmployeeRepository.saveAll(eStoreMapper
                .toEntityFromIds(employeeDTO.getAvailableElectroTypeIds(), employee.getId()));
    }

    @Override
    @Transactional
    public void updateEmployee(Long employeeId, EmployeePutDTO employeePutDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        EmployeeDTO mainData = employeePutDTO.getEmployeeDTO();
        employee.setFirstName(mainData.getFirstName());
        employee.setLastName(mainData.getLastName());
        employee.setPatronymic(mainData.getPatronymic());
        employee.setGender(mainData.getGender());
        employee.setPositionId(mainData.getPositionId());
        employee.setShopId(mainData.getShopId());
        employeeRepository.save(employee);
        electroEmployeeRepository.saveAll(eStoreMapper.
                toEntityFromIds(employeePutDTO.getAvailableElectroTypeIds(), employee.getId()));
    }

    /**
     * Находит до {@code limit} лучших сотрудников с должностью {@code positionId} исходя из {@code metrics},
     * за последние 365 дней.
     *
     * @param positionId идентификатор должности.
     * @param metrics    список метрик, по которым производится выборка.
     * @param limit      количество сотрудников.
     * @return Список сотрудников и их производительности (сумма и количество проданных товаров),
     * в виде {@link EmployeePerformanceDTO}.
     * @throws ResourceNotFoundException в случае, если нет должности с указанными идентификаторами.
     * @see Metric
     */
    @Override
    public List<EmployeePerformanceDTO> getLastYearBestEmployees(long positionId, List<Metric> metrics, int limit, long year)
            throws ResourceNotFoundException {
        Optional<PositionType> position = positionRepository.findById(positionId);
        if (position.isEmpty()) throw new ResourceNotFoundException("There is no position with id: " + positionId);

        // Определяем границы периода выборки: от (текущая дата - (кол-во лет * 365 дней)) до (текущая дата)
        LocalDateTime start = LocalDate.now().minusYears(year).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        Date from = Date.from(start.atZone(zone).toInstant());
        Date to = Date.from(end.atZone(zone).toInstant());

        Sort sort = Sort.unsorted();
        if (metrics.isEmpty()) {
            sort = Sort.by(Sort.Order.desc("soldCount"));
        } else {
            for (Metric metric : metrics) {
                sort = sort.and(Sort.by(Sort.Order.desc(metric.name())));
            }
        }

        return employeeRepository.findLastYearBestEmployees(positionId, from, to, PageRequest.of(0, limit, sort));
    }

    /**
     * Находит сотрудинка с должностью {@code positionId}, продавшего наибольшее количество товаров.
     *
     * @param positionId идентификатор должности.
     * @return Лучшего сотрудника и количество проданных товаров в виде {@link BestEmployeeDTO}.
     * @throws ResourceNotFoundException в случае, если нет должности с указанными идентификаторами
     *                                   или для указанной должности не было продаж.
     */
    @Override
    public BestEmployeeDTO getBestEmployee(Long positionId) throws ResourceNotFoundException {
        positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("No such position with id: " + positionId));

        List<BestEmployeeDTO> bestEmployeeDTO = employeeRepository
                .getBestEmployee(positionId, PageRequest.of(0, 1));
        return bestEmployeeDTO.stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no purchase for given position"));
    }

    /**
     * Находит сотрудинка с должностью {@code positionId}, продавшего наибольшее количество товаров с {@code itemId}.
     *
     * @param positionId идентификатор должности.
     * @param itemTypeId идентификатор типа товара.
     * @return Лучшего сотрудника и количество проданных товаров в виде {@link BestEmployeeDTO}.
     * @throws ResourceNotFoundException в случае, если нет должности с указанными идентификаторами
     *                                   или для указанной должности не было продаж.
     */
    public BestEmployeeDTO getBestEmployeeWithConcreteItem(Long positionId, Long itemTypeId) throws ResourceNotFoundException {
        positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("No such position with id: " + positionId));
        electroItemRepository.findById(itemTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("No such item with id: " + itemTypeId));

        List<BestEmployeeDTO> bestEmployeeDTO = employeeRepository
                .getBestEmployeeWithItemType(positionId, itemTypeId, PageRequest.of(0, 1));

        return bestEmployeeDTO.stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no purchase for given position"));
    }
}
