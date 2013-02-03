package edu.hm.gamedev.server.storage;

import edu.hm.gamedev.server.storage.dto.LogEntryDto;

/**
 * Interface for a storage that stores client logs.
 */
public interface ClientLogStorage {

  /**
   * Inserts a given log entry.
   *
   * @param logEntry Log entry.
   */
  void insert(LogEntryDto logEntry);
}
