package edu.hm.gamedev.server.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hm.gamedev.server.storage.dto.LogEntryDto;

public class MemoryClientLogStorage implements ClientLogStorage {

  private final List<LogEntryDto> logEntries = Collections
      .synchronizedList(new ArrayList<LogEntryDto>());

  @Override
  public void insert(LogEntryDto logEntry) {
    logEntries.add(logEntry);
  }
}
