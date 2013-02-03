package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class GameDeleted extends Packet {

  private final String game;

  public GameDeleted(Game game) {
    super(Type.GAME_DELETED);
    this.game = game.getName();
  }

  public String getGame() {
    return game;
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

    GameDeleted that = (GameDeleted) o;

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
    return "GameDeleted{" +
           "game='" + game + '\'' +
           '}';
  }
}
