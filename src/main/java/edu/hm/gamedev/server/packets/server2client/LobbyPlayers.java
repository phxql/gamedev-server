package edu.hm.gamedev.server.packets.server2client;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class LobbyPlayers extends Packet {

  private final Collection<String> players;

  public LobbyPlayers(Iterable<Player> players) {
    super(Type.LOBBY_PLAYERS);

    this.players = Lists.newArrayList(Iterables.transform(players, new Function<Player, String>() {
      @Override
      public String apply(Player player) {
        return player.getNickname();
      }
    }));
  }

  public Iterable<String> getPlayers() {
    return players;
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

    LobbyPlayers that = (LobbyPlayers) o;

    if (players != null ? !players.equals(that.players) : that.players != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (players != null ? players.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "LobbyPlayers{" +
           "players=" + players +
           '}';
  }
}
