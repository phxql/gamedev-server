package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Sets the map storage for a given game and a given map to a given content.
 */
public class SetMapStorage extends Packet {

  /**
   * Name of the game.
   */
  private final String game;

  /**
   * Map,
   */
  private final String map;

  /**
   * Content.
   */
  private final JsonNode content;

  @JsonCreator
  public SetMapStorage(@JsonProperty("game") String game, @JsonProperty("map") String map,
                       @JsonProperty("content") JsonNode content) {
    super(Type.SET_MAP_STORAGE);
    this.game = game;
    this.map = map;
    this.content = content;
  }

  @Override
  public String toString() {
    return "SetMapStorage{" +
           "game='" + game + '\'' +
           ", map='" + map + '\'' +
           ", content=" + content +
           '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    SetMapStorage that = (SetMapStorage) o;

    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }
    if (game != null ? !game.equals(that.game) : that.game != null) {
      return false;
    }
    if (map != null ? !map.equals(that.map) : that.map != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (game != null ? game.hashCode() : 0);
    result = 31 * result + (map != null ? map.hashCode() : 0);
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }

  public String getMap() {

    return map;
  }

  public String getGame() {

    return game;
  }

  public JsonNode getContent() {
    return content;
  }
}
