package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.repo.EmployeeRepository;
import ru.isands.test.estore.domain.util.CsvFileProcessor;
import ru.isands.test.estore.domain.util.processors.priority.Priority;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeCsvFileProcessor implements CsvFileProcessor {
    private final EmployeeRepository employeeRepository;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Функция, собирающая объект Employee, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, Employee> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 8)
            return null;
        try {
            Employee employee = new Employee();
            employee.setId(Long.parseLong(fields[0]));
            employee.setLastName(fields[1]);
            employee.setFirstName(fields[2]);
            employee.setPatronymic(fields[3]);
            employee.setBirthDate(dateFormat.parse(fields[4]));
            employee.setPositionId(Long.parseLong(fields[5]));
            employee.setShopId(Long.parseLong(fields[6]));
            employee.setGender(fields[7].equals("1"));
            return employee;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    };

    @Override
    public Priority getPriority() {
        return Priority.MASTER;
    }

    @Override
    public String getFileName() {
        return "Employee.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<Employee> employeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        employeeRepository.saveAll(employeeList);
    }
}
