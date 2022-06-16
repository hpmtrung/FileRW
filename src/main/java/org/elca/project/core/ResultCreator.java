package org.elca.project.core;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultCreator {
  private final SortMode sortMode;
  private final int limit;

  private ResultCreator(Builder builder) {
    this.sortMode = builder.sortMode;
    this.limit = builder.limit;
  }

  public int getLimit() {
    return limit;
  }

  public SortMode getSortMode() {
    return sortMode;
  }

  public List<String> apply(List<String> list) {
    Stream<String> stream =
        list.stream()
            .sorted(
                sortMode.equals(SortMode.ASC)
                    ? Comparator.naturalOrder()
                    : Comparator.reverseOrder());
    if (limit > 0) {
      stream = stream.limit(limit);
    }
    return stream.collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "ResultCreator{" + "sortMode=" + sortMode + ", limit=" + limit + '}';
  }

  public enum SortMode {
    ASC,
    DESC
  }

  public static class Builder {
    private SortMode sortMode = SortMode.ASC;
    private int limit = -1;

    public Builder() {}

    public Builder withSortMode(SortMode sortMode) {
      this.sortMode = sortMode;
      return this;
    }

    public Builder withLimit(int limit) {
      this.limit = limit;
      return this;
    }

    public ResultCreator build() {
      return new ResultCreator(this);
    }
  }
}