package org.elca.project.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TemplateReader {

  protected static final String NAME_HEADER = "Name";
  protected static final String COUNTRY_HEADER = "Country";
  protected static final String IS_HEADQUATER_HEADER = "isHeadQuarter";

  protected static final String FILTER_COUNTRY = "CH";

  private boolean isLargeFile(File file) {
    try {
      BasicFileAttributes attrs = Files.readAttributes(file.toPath(),
          BasicFileAttributes.class);
      if (attrs.size() > 1024 * 1024) {
        return true;
      }
    } catch (IOException e) {
      System.err.println("Can not read file size!");
      e.printStackTrace();
    }
    return false;
  }

  public void search(File file, ResultCreator resultCreator) {
    printInfo();
    try {
      List<String> items = new ArrayList<>();
      if (isLargeFile(file)) {
        System.out.println("Find a large file...");
        getItemsFromLargeFile(file, items);
      } else {
        System.out.println("Find a small file...");
        getItemsFromSmallFile(file, items);
      }
      printResult(items, resultCreator);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void printInfo() {
    System.out.println("Process by unknown reader...");
  }

  protected abstract void getItemsFromSmallFile(File file, List<String> items) throws IOException;

  protected abstract void getItemsFromLargeFile(File file,
                                                List<String> items) throws IOException;

  protected void printResult(List<String> items, ResultCreator resultCreator) {
    items = resultCreator.apply(items);
    System.out.println("Total matched items: " + items.size());
    System.out.println(Arrays.toString(items.toArray()));
  }
}