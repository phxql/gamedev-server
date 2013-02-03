package edu.hm.gamedev.server.packets.server2client;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;
import edu.hm.gamedev.server.packets.dto.GameDto;

public class OpenGames extends Packet {

  private final Collection<GameDto> games;

  public OpenGames(Iterable<Game> games) {
    super(Type.OPEN_GAMES);

    this.games = Lists.newArrayList(Iterables.transform(games, new Function<Game, GameDto>() {
      @Override
      public GameDto apply(Game game) {
        return new GameDto(game);
      }
    }));
  }

  public Collection<GameDto> getGames() {
    return games;
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

    OpenGames openGames = (OpenGames) o;

    if (games != null ? !games.equals(openGames.games) : openGames.games != null) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "OpenGames{" +
           "games=" + games +
           '}';
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (games != null ? games.hashCode() : 0);
    return result;
  }
}
