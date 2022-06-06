package org.elca.project.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class DataReader {
  public void readFile(File file) throws IOException {
    FileInputStream fis = null;
    Scanner sc = null;
    try {
      fis = new FileInputStream(file);
      sc = new Scanner(fis, "UTF-8");
      while (sc.hasNextLine()) {
        String line = sc.nextLine();

      }
      // Scanner suppresses exceptions
      if (sc.ioException() != null) {
        throw sc.ioException();
      }
    } catch (FileNotFoundException e) {

    } catch (Exception e) {
    } finally {
      if (fis != null) {
        fis.close();
      }
      if (sc != null) {
        sc.close();
      }
    }
  }

  public abstract void processData();

  public abstract void printResult();
}