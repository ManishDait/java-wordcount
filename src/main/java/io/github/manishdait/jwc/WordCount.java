package io.github.manishdait.jwc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WordCount {
  static boolean showByteCount = false;
  static boolean showWordCount = false;
  static boolean showLineCount = false;
  static boolean showCharCount = false;

  public static void main(String[] args) throws IOException {
    

    List<String> filenames = new ArrayList<>();

    for (String arg : args) {
      if (arg.equals("-c") || arg.equals("--bytes")) {
        showByteCount = true;
      } else if (arg.equals("-l") || arg.equals("--lines")) {
        showLineCount = true;
      } else if (arg.equals("-w") || arg.equals("--words")) {
        showWordCount = true;
      } else if (arg.equals("-m") || arg.equals("--chars")) {
        showCharCount = true;
      } else if (arg.equals("--help")) {
        printHelp();
        System.exit(0);
      } else if (arg.equals("--version")) {
        printVersion();
        System.exit(0);
      } else {
        filenames.add(arg);
      }
    }

    if (filenames.size() == 0) {
      if (!(System.in.available() > 0)) {
        System.out.println("Invalid input.");
        printUsage();
        System.exit(0);
      }

      Scanner scan = new Scanner(System.in);

      String filename = System.currentTimeMillis() + "-temp.txt";

      File tempFile = new File(filename);
      tempFile.createNewFile();

      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

      int character;
      while ((character = reader.read()) != -1) {
        writer.write(character);
      }

      writer.close();
      reader.close();
      scan.close();

      printResult(filename);
      System.out.println("");
      tempFile.delete();
    } else {
      for (String filename : filenames) {
        printResult(filename);
        System.out.println(filename);
      }
    }
  }

  private static void printResult(String filename) throws IOException {
    File file = new File(filename);
    if(!file.exists()) {
      System.out.println("File '\u001B[33m" + filename + "\u001B[37m' does not exist.");
      printUsage();
      System.exit(0);;
    }
    if (showByteCount) {
      System.out.printf("%8d ", countByte(filename));
    }
    if (showWordCount) {
      System.out.flush();
      System.out.printf("%8d ", countWord(filename));
    }
    if (showLineCount) {
      System.out.printf("%8d ", countLine(filename));
    }
    if (showCharCount) {
      System.out.printf("%8d ", countChar(filename));
    }
    if (!showByteCount && !showCharCount && !showLineCount && !showWordCount) {
      System.out.printf("%8d %8d %8d ",countLine(filename), countWord(filename), countByte(filename));
    }
  }

  private static void printUsage() {
    System.out.println("Usage: jwc [OPTION]... [FILE]...\n");
    System.out.println("Try 'jwc --help' for more information.");
  }

  private static void printHelp() {
    System.out.println("Usage: jwc [OPTION]... [FILE]...");
    System.out.println("Print newline, word, byte, and character counts for each FILE.");
    System.out.println("A word is a non-zero-length sequence of characters delimited by white space.\n");
    System.out.println("The options below may be used to select which counts are printed, always in");
    System.out.println("the following order: newline, word, character, byte, maximum line length.");

    System.out.println("  -c, --bytes            print the byte counts");
    System.out.println("  -m, --chars            print the character counts");
    System.out.println("  -l, --lines            print the line counts");
    System.out.println("  -w, --words            print the word counts");
    System.out.println("      --help             display help");
    System.out.println("      --version          output version information");
  }

  private static void printVersion() {
    System.out.println("jwc v-1.0.0 2023-01-20");
    System.out.println("Written by Manish Dait.");
  }

  protected static long countByte(String filename) throws IOException {
    long byteCount = 0;

    FileInputStream stream = new FileInputStream(filename);

    while (stream.read() != -1) {
      byteCount++;
    }

    stream.close();

    return byteCount;
  }

  protected static long countLine(String filename) throws IOException {
    long lineCount = 0;

    File file = new File(filename);
    Scanner stream = new Scanner(file);

    while (stream.hasNextLine()) {
      stream.nextLine();
      lineCount++;
    }

    stream.close();

    return lineCount;
  }

  protected static long countWord(String filename) throws IOException {
    long wordCount  = 0;

    File file = new File(filename);
    Scanner stream = new Scanner(file);

    while (stream.hasNext()) {
      stream.next();
      wordCount++;
    }

    stream.close();

    return wordCount;
  }

  protected static long countChar(String filename) throws IOException {
    long charCount = 0;


    BufferedReader stream = new BufferedReader(new FileReader(filename));

    while (stream.read() != -1) {
      charCount++;
    }

    stream.close();

    return charCount;
  }
}
