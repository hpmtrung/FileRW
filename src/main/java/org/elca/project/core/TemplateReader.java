package org.elca.project.core;

import org.elca.project.search.NativeSearch;
import org.elca.project.search.SmartSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class TemplateReader {

  protected static final String NAME_HEADER = "Name";
  protected static final String COUNTRY_HEADER = "Country";
  protected static final String IS_HEADQUATER_HEADER = "isHeadQuarter";

  protected static final String FILTER_COUNTRY = "CH";

  private boolean isLargeFile(File file) {

    return false;
  }

  public void search(File file, ResultCreator resultCreator) {
    printInfo();
    try {
      List<String> items = new ArrayList<>();
      getItemsFromFile(file, items);
      printResult(items, resultCreator);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void printInfo() {
    System.out.println("Process by unknown reader...");
  }

  protected abstract void getItemsFromFile(File file, List<String> items) throws IOException;

  protected void printResult(List<String> items, ResultCreator resultCreator) {
    items = resultCreator.apply(items);
    System.out.println("Total matched items: " + items.size());
    System.out.println(Arrays.toString(items.toArray()));
  }
}