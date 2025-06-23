package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.repo.ElectroShopRepository;
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
public class ElectroShopCsvFileProcessor implements CsvFileProcessor {
    private final ElectroShopRepository electroShopRepository;

    /**
     * Функция, собирающая объект ElectroShop, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, ElectroShop> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 3)
            return null;
        try {
            ElectroShop electroShop = new ElectroShop();
            electroShop.setShopId(Long.parseLong(fields[0]));
            electroShop.setElectroItemId(Long.parseLong(fields[1]));
            electroShop.setCount(Integer.parseInt(fields[2]));
            return electroShop;
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
        return "ElectroShop.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<ElectroShop> electroEmployeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        electroShopRepository.saveAll(electroEmployeeList);
    }
}
