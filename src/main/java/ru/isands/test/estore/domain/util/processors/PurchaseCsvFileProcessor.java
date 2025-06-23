package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.Purchase;
import ru.isands.test.estore.dao.repo.PurchaseRepository;
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
public class PurchaseCsvFileProcessor implements CsvFileProcessor {
    private final PurchaseRepository purchaseRepository;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    /**
     * Функция, собирающая объект Purchase, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, Purchase> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 6)
            return null;
        try {
            Purchase purchase = new Purchase();
            purchase.setId(Long.parseLong(fields[0]));
            purchase.setElectroId(Long.parseLong(fields[1]));
            purchase.setEmployeeId(Long.parseLong(fields[2]));
            purchase.setPurchaseDate(dateFormat.parse(fields[3]));
            purchase.setType(Long.parseLong(fields[4]));
            purchase.setShopId(Long.parseLong(fields[5]));
            return purchase;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    };

    @Override
    public Priority getPriority() {
        return Priority.TRANSACTIONAL;
    }

    @Override
    public String getFileName() {
        return "Purchase.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<Purchase> employeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        purchaseRepository.saveAll(employeeList);
    }
}
