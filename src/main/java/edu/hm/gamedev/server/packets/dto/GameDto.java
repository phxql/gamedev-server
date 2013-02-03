package edu.hm.gamedev.server.packets.dto;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.codehaus.jackson.JsonNode;

import java.util.Collection;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;

/**
 * Class to transfer game informations to a client.
 */
public class GameDto {

  private final String name;
  private final String type;
  private final int slots;
  private final Collection<String> takers;
  private final JsonNode info;

  public GameDto(Game game) {
    this(game.getName(), game.getType(), game.getTakers(), game.getSlots(), game.getInfo());
  }

  public GameDto(String name, String type, Iterable<Player> takers, int slots, JsonNode info) {
    this.name = name;
    this.type = type;
    this.slots = slots;
    this.info = info;
    this.takers = Lists.newArrayList(Iterables.transform(takers, new Function<Player, String>() {
      @Override
      public String apply(Player player) {
        return player.getNickname();
      }
    }));
  }

  public JsonNode getInfo() {
    return info;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getSlots() {
    return slots;
  }

  public Collection<String> getTakers() {
    return takers;
  }

  @Override
  public String toString() {
    return "GameDto{" +
           "name='" + name + '\'' +
           ", type='" + type + '\'' +
           ", slots=" + slots +
           ", takers=" + takers +
           ", info=" + info +
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

    GameDto gameDto = (GameDto) o;

    if (slots != gameDto.slots) {
      return false;
    }
    if (info != null ? !info.equals(gameDto.info) : gameDto.info != null) {
      return false;
    }
    if (name != null ? !name.equals(gameDto.name) : gameDto.name != null) {
      return false;
    }
    if (takers != null ? !takers.equals(gameDto.takers) : gameDto.takers != null) {
      return false;
    }
    if (type != null ? !type.equals(gameDto.type) : gameDto.type != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + slots;
    result = 31 * result + (takers != null ? takers.hashCode() : 0);
    result = 31 * result + (info != null ? info.hashCode() : 0);
    return result;
  }
}

