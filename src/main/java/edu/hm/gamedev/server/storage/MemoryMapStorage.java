package edu.hm.gamedev.server.storage;

import org.codehaus.jackson.JsonNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A map storage which stores the contents in memory.
 */
public class MemoryMapStorage implements MapStorage {

  /**
   * Storage.
   */
  private final
  Map<String, JsonNode>
      storage =
      Collections.synchronizedMap(new HashMap<String, JsonNode>());

  @Override
  public JsonNode load(String game, String map) {
    return storage.get(game + "." + map);
  }

  @Override
  public void save(String game, String map, JsonNode content) {
    storage.put(game + "." + map, content);
  }
}
