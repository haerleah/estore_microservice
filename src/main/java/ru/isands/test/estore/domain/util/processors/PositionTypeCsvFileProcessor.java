package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.PositionType;
import ru.isands.test.estore.dao.repo.PositionRepository;
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
public class PositionTypeCsvFileProcessor implements CsvFileProcessor {
    private final PositionRepository positionRepository;

    /**
     * Функция, собирающая объект PositionType, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, PositionType> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 2)
            return null;
        try {
            PositionType positionType = new PositionType();
            positionType.setId(Long.parseLong(fields[0]));
            positionType.setName(fields[1]);
            return positionType;
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
        return "PositionType.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<PositionType> employeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        positionRepository.saveAll(employeeList);
    }
}
