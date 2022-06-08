package org.elca.project.reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.elca.project.core.TemplateReader;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVReader extends TemplateReader {

  @Override
  protected void printInfo() {
    System.out.println("Process by CSV reader...");
  }

  @Override
  protected void getItemsFromSmallFile(File file, List<String> items) throws IOException {
    Reader in = new FileReader(file, StandardCharsets.UTF_8);
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().build().parse(in);
    List<CSVRecord> companies = new ArrayList<>();
    records.forEach(companies::add);
    items.clear();
    companies.stream()
        .filter(CSVReader::isRecordValid)
        .map(company -> company.get(NAME_HEADER))
        .forEach(items::add);
  }

  @Override
  protected void getItemsFromLargeFile(File file, List<String> items) throws IOException {
    Reader in = new FileReader(file, StandardCharsets.UTF_8);
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().build().parse(in);
    for (CSVRecord record : records) {
      String name = record.get(NAME_HEADER);
      if (isRecordValid(record)) {
        items.add(name);
      }
    }
  }

  private static boolean isRecordValid(CSVRecord record) {
    String country = record.get(COUNTRY_HEADER);
    String isHeadQuater = record.get(IS_HEADQUATER_HEADER);
    return (country != null
        && country.equals(FILTER_COUNTRY)
        && isHeadQuater != null
        && isHeadQuater.equals("1"));
  }
}