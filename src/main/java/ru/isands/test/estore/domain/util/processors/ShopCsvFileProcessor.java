package ru.isands.test.estore.domain.util.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.repo.ShopRepository;
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
public class ShopCsvFileProcessor implements CsvFileProcessor {
    private final ShopRepository shopRepository;

    /**
     * Функция, собирающая объект Shop, в соответсвии с заданной стркутурой файлов.
     */
    private final Function<String, Shop> readCsvLine = (line) -> {
        String[] fields = line.split(";");
        if (fields.length != 3)
            return null;
        try {
            Shop shop = new Shop();
            shop.setId(Long.parseLong(fields[0]));
            shop.setName(fields[1]);
            shop.setAddress(fields[2]);
            return shop;
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
        return "Shop.csv";
    }

    @Override
    public void process(InputStream csvStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, "windows-1251"));
        List<Shop> employeeList = reader.lines().skip(1).map(readCsvLine).collect(Collectors.toList());
        shopRepository.saveAll(employeeList);
    }
}
