package org.elca.project.search;

import org.elca.project.core.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class NativeSearch implements SearchStrategy {

  @Override
  public List<String> search(List<String> c, String value) {
    System.out.println("Native search");
    return c.stream().filter(e -> e.equals(value)).collect(Collectors.toList());
  }
}