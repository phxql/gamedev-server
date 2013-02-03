package edu.hm.gamedev.server.settings;

public final class Settings {
  private Settings() {
  }

  /**
   * Settings for emails.
   */
  public static class EmailSettings {
    public static final String FROM = "";
    public static final String USER = "";
    public static final String PASSWORD = "";
    public static final int PORT = 25;
    public static final String SMTP_HOST = "";
  }

  /**
   * Settings for email verification.
   */
  public static class EmailVerification {
    public static final boolean ENABLED = false;
  }

  /**
   * Settings for password reset.
   */
  public static class PasswordReset {
    public static final boolean ENABLED = false;
  }

  /**
   * Settings for buffered messages.
   */
  public static class BufferedMessages {
    public static final int DELAY_MS = 50;
  }

  /**
   * Settings for error reporting.
   */
  public static class ErrorReporting {
    /**
     * If true, the InternalServerError packet contains the stack trace of the exception.
     */
    public static final boolean STACK_TRACE_IN_PACKET = true;
  }

  /**
   * Settings for client threading.
   */
  public static class ClientThreading {
    /**
     * The default setting if client sending blocks.
     */
    public static final boolean CLIENT_SENDING_BLOCKS_DEFAULT = false;
  }

  /**
   * Settings for dead client detection.
   */
  public static class DeadClientDetection {
    /**
     * Ping a client if it is inactive for at least n milliseconds.
     */
    public static final int PING_THRESHOLD_MS = 10 * 1000;

    /**
     * If the client doesn't respond in n milliseconds to a ping, it will be removed.
     */
    public static final int PING_RESPONSE_TIME_MS = 5 * 1000;

    /**
     * Remove a client if it is inactive for at least n milliseconds.
     */
    public static final int REMOVE_THRESHOLD_MS = PING_THRESHOLD_MS + PING_RESPONSE_TIME_MS;

    /**
     * Check for dead clients every n milliseconds.
     */
    public static final int CHECK_INTERVAL_MS = 1000;

    /**
     * If true, dead clients will be detected and removed.
     */
    public static final boolean ENABLED = true;
  }

  public static class Token {
    /**
     * Allowed chars.
     */
    public static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

    /**
     * Length of the generated token.
     */
    public static final int TOKEN_LENGTH = 5;
  }

}
