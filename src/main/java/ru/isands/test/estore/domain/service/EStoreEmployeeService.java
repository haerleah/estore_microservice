package ru.isands.test.estore.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.isands.test.estore.dto.*;
import ru.isands.test.estore.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Сервис для работы с сотрудниками
 */
public interface EStoreEmployeeService {
    Page<EmployeeDTO> getEmployees(Pageable pageable);
    void saveEmployee(EmployeeCreateDTO employeeDTO);
    void updateEmployee(Long employeeId, EmployeePutDTO employeeDTO);
    List<EmployeePerformanceDTO> getLastYearBestEmployees(long positionId, List<Metric> metrics, int limit, long year)
            throws ResourceNotFoundException;
    BestEmployeeDTO getBestEmployeeWithConcreteItem(Long positionId, Long itemTypeId) throws ResourceNotFoundException;
    BestEmployeeDTO getBestEmployee(Long positionId) throws ResourceNotFoundException;
}
