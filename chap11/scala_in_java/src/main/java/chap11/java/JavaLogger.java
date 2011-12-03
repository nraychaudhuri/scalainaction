package chap11.java;

import java.io.*;
import chap11.scala.*;

public class JavaLogger extends DualLogger {
  public void logIt(String m) {
    log(m);
  }  
}
