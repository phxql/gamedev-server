package edu.hm.gamedev.server.storage;

import org.codehaus.jackson.JsonNode;

/**
 * Interface for a map storage.
 */
public interface MapStorage {

  /**
   * Loads the content for a given game and a given map.
   *
   * @param game Game.
   * @param map  Map.
   * @return Content. Can be null.
   */
  JsonNode load(String game, String map);

  /**
   * Saves the given content for a given map and a given game.
   *
   * @param game    Game.
   * @param map     Map.
   * @param content Content to save.
   */
  void save(String game, String map, JsonNode content);
}
