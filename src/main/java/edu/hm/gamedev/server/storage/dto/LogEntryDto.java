package edu.hm.gamedev.server.storage.dto;

import java.sql.Timestamp;

/**
 * DTO class for log entries.
 */
public class LogEntryDto {

  /**
   * Message.
   */
  private final String message;

  /**
   * Level.
   *
   * Higher values means more severity.
   */
  private final int level;

  /**
   * Timestamp.
   */
  private final Timestamp timestamp;

  /**
   * Email of the player which sent the log entry.
   */
  private final String email;

  public LogEntryDto(Timestamp timestamp, String message, int level, String email) {
    this.timestamp = (Timestamp)timestamp.clone();
    this.message = message;
    this.level = level;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public Timestamp getTimestamp() {
    return (Timestamp)timestamp.clone();
  }

  public String getMessage() {
    return message;
  }

  public int getLevel() {
    return level;
  }
}
