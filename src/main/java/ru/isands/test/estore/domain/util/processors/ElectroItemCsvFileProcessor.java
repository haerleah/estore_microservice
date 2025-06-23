package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
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
public class ElectroItemCsvFileProcessor implements CsvFileProcessor {
    private final ElectroItemRepository electroItemRepository;

    /**
     * Функция, собирающая объект ElectroItem, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, ElectroItem> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 7)
            return null;
        try {
            ElectroItem electroItem = new ElectroItem();
            electroItem.setId(Long.parseLong(fields[0]));
            electroItem.setName(fields[1]);
            electroItem.setTypeId(Long.parseLong(fields[2]));
            electroItem.setPrice(Long.parseLong(fields[3]));
            electroItem.setCount(Long.parseLong(fields[4]));
            electroItem.setArchive(fields[5].equals("1"));
            electroItem.setDescription(fields[6]);
            return electroItem;
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
        return "ElectroItem.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<ElectroItem> electroEmployeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        electroItemRepository.saveAll(electroEmployeeList);
    }
}
