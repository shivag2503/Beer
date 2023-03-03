package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCsv() throws Exception {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> records = beerCsvService.convertCSV(file);

        System.out.println(records.size());

        assertThat(records.size()).isGreaterThan(0);
    }
}
