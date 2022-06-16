package org.elca.project.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchFile {

  private final WatchService watcher;
  private final WatchKey entry;
  private final String filename;

  public WatchFile(Path dir) throws IOException, IllegalArgumentException {
    if (Files.isDirectory(dir)) {
      throw new IllegalArgumentException("Path is not a file!");
    } else {
      this.filename = dir.getFileName().toString();
    }
    this.watcher = FileSystems.getDefault().newWatchService();
    this.entry = dir.getParent().register(watcher, ENTRY_MODIFY, ENTRY_DELETE);
  }

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>) event;
  }

  public void watch(Consumer<WatchEvent.Kind> callback) throws InterruptedException {
    while (true) {
      final WatchKey key = watcher.take();

      for (WatchEvent<?> event : key.pollEvents()) {
        System.out.println("Event name: " + event.kind().name());
        Path changed = (Path) event.context();
        if (changed.endsWith(this.filename)) {
          callback.accept(event.kind());
        }
      }

      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) break;
    }
  }
}