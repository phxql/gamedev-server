package edu.hm.gamedev.server.packets.server2client;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;
import edu.hm.gamedev.server.packets.dto.TakerDto;

/**
 * Takers of the current game.
 */
public class Takers extends Packet {

  /**
   * Takers.
   */
  private Collection<TakerDto> takers;

  public Takers(Iterable<Player> takers) {
    super(Type.TAKERS);

    this.takers =
        Lists.newArrayList(Iterables.transform(takers, new Function<Player, TakerDto>() {
          @Override
          public TakerDto apply(Player player) {
            return new TakerDto(player.getNickname(), player.isReady());
          }
        }));
  }

  @Override
  public String toString() {
    return "Takers{" +
           "takers=" + takers +
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

    Takers takers1 = (Takers) o;

    if (takers != null ? !takers.equals(takers1.takers) : takers1.takers != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (takers != null ? takers.hashCode() : 0);
    return result;
  }

  public Collection<TakerDto> getTakers() {
    return takers;
  }
}

