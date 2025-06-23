package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.ElectroEmployee;
import ru.isands.test.estore.dao.repo.ElectroEmployeeRepository;
import ru.isands.test.estore.domain.util.CsvFileProcessor;
import ru.isands.test.estore.domain.util.processors.priority.Priority;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElectroEmployeeCsvFileProcessor implements CsvFileProcessor {
    private final ElectroEmployeeRepository electroEmployeeRepository;

    /**
     * Функция, собирающая объект ElectroEmployee, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, ElectroEmployee> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 2)
            return null;
        try {
            ElectroEmployee electroEmployee = new ElectroEmployee();
            electroEmployee.setEmployeeId(Long.parseLong(fields[0]));
            electroEmployee.setElectroTypeId(Long.parseLong(fields[1]));
            return electroEmployee;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    };

    @Override
    public Priority getPriority() {
        return Priority.JUNCTION;
    }

    @Override
    public String getFileName() {
        return "ElectroEmployee.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<ElectroEmployee> electroEmployeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        electroEmployeeRepository.saveAll(electroEmployeeList);
    }
}
