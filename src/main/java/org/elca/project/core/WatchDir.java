package org.elca.project.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchDir {

  private final WatchService watcher;
  // Map of registered directory (called as key)
  private final Map<WatchKey, Path> keys;
  private boolean trace = false;

  public WatchDir(Path dir) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();
    register(dir);
    this.trace = true;
  }

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>) event;
  }

  private void register(Path dir) throws IOException {
    WatchKey key = dir.register(watcher, ENTRY_MODIFY, ENTRY_DELETE);
    if (trace) {
      Path prev = keys.get(key);
      if (prev == null) {
        System.out.format("register: %s\n", dir);
      } else {
        if (!dir.equals(prev)) {
          System.out.format("update: %s -> %s\n", prev, dir);
        }
      }
    }
    keys.put(key, dir);
  }

  public void processEvents(Supplier<Void> modifyCallback,
                            Supplier<Void> deleteCallback) {
    while (true) {
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }
      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        // TBD - provide example of how OVERFLOW event is handled
        if (kind == OVERFLOW) {
          continue;
        }

        // Context for directory entry event is the file name of entry
        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

        // print out event
        System.out.format("%s: %s\n", kind.name(), child);

        if (kind == ENTRY_MODIFY) {
          modifyCallback.get();
        } else if (kind == ENTRY_DELETE) {
          deleteCallback.get();
          System.out.println("Registered dir is deleted! Corrupt!");
        }
      }
      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);

        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }
}