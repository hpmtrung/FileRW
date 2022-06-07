package org.elca.project.core;

import org.elca.project.reader.CSVReader;

public class DataReader {

  private static final DataReader INSTANCE = new DataReader();

  public static DataReader getInstance() { return INSTANCE; }

  private TemplateReader reader = new CSVReader();

  private DataReader() {
  }

  public void changeReader(TemplateReader reader) {
    if (reader == null) throw new NullPointerException();
    this.reader = reader;
  }

  public TemplateReader getReader() {
    return reader;
  }
}