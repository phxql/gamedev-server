package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;
import edu.hm.gamedev.server.packets.dto.GameDto;

public class GameCreated extends Packet {

  private final GameDto game;

  public GameCreated(Game game) {
    super(Type.GAME_CREATED);
    this.game = new GameDto(game);
  }

  @Override
  public String toString() {
    return "GameCreated{" +
           "game=" + game +
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

    GameCreated that = (GameCreated) o;

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

  public GameDto getGame() {
    return game;
  }
}
