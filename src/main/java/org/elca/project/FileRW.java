package org.elca.project;

import org.elca.project.core.DataReader;
import org.elca.project.core.ResultCreator;
import org.elca.project.core.WatchFile;
import org.elca.project.reader.CSVReader;
import org.elca.project.reader.XMLReader;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Arrays;

@CommandLine.Command(name = "filerw", mixinStandardHelpOptions = true, version = "FileRW 1.0")
public class FileRW implements Runnable {

  private final DataReader dataReader = DataReader.getInstance();

  private WatchFile watchFile = null;
  private ResultCreator resultCreator = null;

  @CommandLine.Spec CommandLine.Model.CommandSpec spec;

  @CommandLine.Option(names = {"-w", "--watch"})
  private boolean watch = false;

  @CommandLine.Parameters(arity = "1", paramLabel = "FILE", description = "input file")
  private File inputFile;

  private ResultCreator.SortMode sort = ResultCreator.SortMode.ASC;
  private int limit = -1;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new FileRW()).execute(args);
    System.exit(exitCode);
  }

  @CommandLine.Option(
      names = {"--sort"},
      description = "sort asc, desc")
  public void setSort(String sort) {
    this.sort =
        Arrays.stream(ResultCreator.SortMode.values())
            .filter(mode -> mode.toString().compareToIgnoreCase(sort) == 0)
            .findFirst()
            .orElseThrow(
                () ->
                    new CommandLine.ParameterException(
                        spec.commandLine(), "Sort option value is invalid"));
  }

  @CommandLine.Option(names = "--limit", description = "number of results")
  public void setLimit(int limit) {
    if (limit <= 0) {
      throw new CommandLine.ParameterException(spec.commandLine(), "Limit " + "value is invalid");
    }
    this.limit = limit;
  }

  @Override
  public void run() {
    this.resultCreator = new ResultCreator.Builder().withLimit(limit).withSortMode(sort).build();
    final String fileName = inputFile.getName();
    try {
      final String ext = fileName.substring(fileName.indexOf('.') + 1).toLowerCase();
      switch (ext) {
        case "csv":
          dataReader.changeReader(new CSVReader());
          break;
        case "xml":
          dataReader.changeReader(new XMLReader());
          break;
        default:
          System.err.println("File extension not support");
          return;
      }
      dataReader.getReader().search(inputFile, resultCreator);
      if (watch) {
        System.out.println("Watching...");
        watchFile = new WatchFile(inputFile.getAbsoluteFile().toPath());
        watchFile.watch(this::handleChange);
      }
    } catch (IndexOutOfBoundsException ex) {
      System.err.println("File name is invalid");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      System.err.println("Can not watch file");
      e.printStackTrace();
    }
  }

  private void handleChange(WatchEvent.Kind eventKind) {
    if (StandardWatchEventKinds.ENTRY_MODIFY.equals(eventKind)) {
      System.out.println("File is modified. Reprocessing...");
      dataReader.getReader().search(inputFile, resultCreator);
    } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(eventKind)) {
      System.out.println("File is deleted. Corrupt!");
    }
  }
}