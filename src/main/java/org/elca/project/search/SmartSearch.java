package org.elca.project.search;

import org.elca.project.core.SearchStrategy;

import java.util.List;

public class SmartSearch implements SearchStrategy {

  @Override
  public List<String> search(List<String> c, String value) {
    System.out.println("Smart search");
    return null;
  }
}