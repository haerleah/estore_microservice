package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.ElectroType;
import ru.isands.test.estore.dao.repo.ElectroTypeRepository;
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
public class ElectroTypeCsvFileProcessor implements CsvFileProcessor {
    private final ElectroTypeRepository electroTypeRepository;

    /**
     * Функция, собирающая объект ElectroType, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, ElectroType> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 2)
            return null;
        try {
            ElectroType electroType = new ElectroType();
            electroType.setId(Long.parseLong(fields[0]));
            electroType.setName(fields[1]);
            return electroType;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    };

    @Override
    public Priority getPriority() {
        return Priority.REFERENCE;
    }

    @Override
    public String getFileName() {
        return "ElectroType.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<ElectroType> electroEmployeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        electroTypeRepository.saveAll(electroEmployeeList);
    }
}
