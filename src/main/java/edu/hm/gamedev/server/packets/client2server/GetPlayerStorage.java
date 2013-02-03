package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class GetPlayerStorage extends Packet {

  private final String game;

  @JsonCreator
  public GetPlayerStorage(@JsonProperty("game") String game) {
    super(Type.GET_PLAYER_STORAGE);

    this.game = game;
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

    GetPlayerStorage that = (GetPlayerStorage) o;

    if (game != null ? !game.equals(that.game) : that.game != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (game != null ? game.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "GetPlayerStorage{" +
           "game='" + game + '\'' +
           '}';
  }

  public String getGame() {
    return game;
  }
}
