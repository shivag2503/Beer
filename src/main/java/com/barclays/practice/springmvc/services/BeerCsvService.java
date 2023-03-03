package com.barclays.practice.springmvc.services;

import com.barclays.practice.springmvc.domain.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    List<BeerCSVRecord> convertCSV(File file);
}
