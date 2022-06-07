package org.elca.project.core;

import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface SearchStrategy {
   List<String> search(List<String> c, String value);
}