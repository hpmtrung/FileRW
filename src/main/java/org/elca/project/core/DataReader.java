package org.elca.project.core;

import org.elca.project.reader.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class DataReader {

  private static final DataReader INSTANCE;

  static {
    INSTANCE = new CSVReader();
  }

  public static DataReader getInstance() {
    return INSTANCE;
  }

  public void processFile(File file) {
    try {
      readFile(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public abstract void readFile(File file) throws IOException;

  public abstract void processData();

  public abstract void printResult();
}