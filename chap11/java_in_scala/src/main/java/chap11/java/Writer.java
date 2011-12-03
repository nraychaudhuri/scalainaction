package chap11.java;
import java.io.*;

public class Writer {
  public void writeToFile(String content) throws IOException {
    File f = File.createTempFile("tmoFile", ".tmp");
    new FileWriter(f).write(content);
  }
}