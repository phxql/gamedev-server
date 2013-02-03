package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Gets the map storage for a given game and a given map.
 */
public class GetMapStorage extends Packet {

  private final String game;

  private final String map;

  @JsonCreator
  public GetMapStorage(@JsonProperty("game") String game, @JsonProperty("map") String map) {
    super(Type.GET_MAP_STORAGE);
    this.game = game;
    this.map = map;
  }

  @Override
  public String toString() {
    return "GetMapStorage{" +
           "game='" + game + '\'' +
           ", map='" + map + '\'' +
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

    GetMapStorage that = (GetMapStorage) o;

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
    return result;
  }

  public String getGame() {

    return game;
  }

  public String getMap() {
    return map;
  }
}
