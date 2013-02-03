package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class JoinGame extends Packet {

  private final String gameName;

  @JsonCreator
  public JoinGame(@JsonProperty("gameName") String gameName) {
    super(Type.JOIN_GAME);
    this.gameName = gameName;
  }

  public String getGameName() {
    return gameName;
  }

  @Override
  public String toString() {
    return "JoinGame{" +
           "gameName='" + gameName + '\'' +
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

    JoinGame joinGame = (JoinGame) o;

    if (gameName != null ? !gameName.equals(joinGame.gameName) : joinGame.gameName != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (gameName != null ? gameName.hashCode() : 0);
    return result;
  }
}
