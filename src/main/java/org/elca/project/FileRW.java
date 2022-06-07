package org.elca.project;

import org.elca.project.core.DataReader;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(name = "filerw", mixinStandardHelpOptions = true,
    version = "FileRW 1.0")
public class FileRW implements Runnable {

  @CommandLine.Option(names = {"-w", "--watch"}, description = "Watch mode")
  private boolean watch = false;

  @CommandLine.Option(names = {"-o", "--out"}, description = "Output file")
  private File outputFile;

  @CommandLine.Parameters(arity = "1..*", paramLabel = "FILE", description =
      "File XML, JSON, CSV, XLSX")
  private File inputFile;

  private final DataReader dataReader = DataReader.getInstance();

  @Override
  public void run() {
    final String fileName = inputFile.getName();
    try {
      final String extension = fileName.substring(fileName.indexOf('.') + 1).toLowerCase();
      if (extension.equals("csv")) {
        System.out.println("Read csv file...\n");
        dataReader.readFile(inputFile);
      } else {
        System.out.println("Else...");
      }
    } catch (IndexOutOfBoundsException ex) {
      System.err.println("File name is invalid");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new FileRW()).execute(args);
    System.exit(exitCode);
  }
}