package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.repo.PurchaseTypeRepository;
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
public class PurchaseTypeCsvFileProcessor implements CsvFileProcessor {
    private final PurchaseTypeRepository purchaseTypeRepository;

    /**
     * Функция, собирающая объект PurchaseType, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, PurchaseType> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 2)
            return null;
        try {
            PurchaseType purchaseType = new PurchaseType();
            purchaseType.setId(Long.parseLong(fields[0]));
            purchaseType.setName(fields[1]);
            return purchaseType;
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
        return "PurchaseType.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<PurchaseType> employeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        purchaseTypeRepository.saveAll(employeeList);
    }
}
