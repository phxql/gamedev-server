package edu.hm.gamedev.server.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Helper for stack traces.
 */
public final class StackTraceHelper {

  private StackTraceHelper() {
  }

  /**
   * Gets the stack trace for an exception as a string.
   *
   * @param throwable Exception.
   * @return Stack trace.
   */
  public static String getStackTrace(Throwable throwable) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);

    throwable.printStackTrace(printWriter);

    return result.toString();
  }
}
