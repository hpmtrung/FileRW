package org.elca.project.reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.elca.project.core.DataReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

public class CSVReader extends DataReader {

  @Override
  public void readFile(File file) throws IOException {
    Reader in = new FileReader(file, Charset.forName("UTF-8"));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
    for (CSVRecord record : records) {
      String id = record.get("id");
      System.out.println(id);
    }
  }

  @Override
  public void processData() {

  }

  @Override
  public void printResult() {

  }
}