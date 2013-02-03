package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class CreateGame extends Packet {

  private final String name;
  private final String gameType;
  private final int slots;
  private final JsonNode info;

  @JsonCreator
  public CreateGame(@JsonProperty("name") String name, @JsonProperty("gameType") String gameType,
                    @JsonProperty("slots") int slots, @JsonProperty("info") JsonNode info) {
    super(Type.CREATE_GAME);
    this.name = name;
    this.gameType = gameType;
    this.slots = slots;
    this.info = info;
  }

  public JsonNode getInfo() {
    return info;
  }

  public String getGameType() {
    return gameType;
  }

  public String getName() {
    return name;
  }

  public int getSlots() {
    return slots;
  }

  @Override
  public String toString() {
    return "CreateGame{" +
           "name='" + name + '\'' +
           ", gameType='" + gameType + '\'' +
           ", slots=" + slots +
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
    if (!super.equals(o)) {
      return false;
    }

    CreateGame that = (CreateGame) o;

    if (slots != that.slots) {
      return false;
    }
    if (gameType != null ? !gameType.equals(that.gameType) : that.gameType != null) {
      return false;
    }
    if (info != null ? !info.equals(that.info) : that.info != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (gameType != null ? gameType.hashCode() : 0);
    result = 31 * result + slots;
    result = 31 * result + (info != null ? info.hashCode() : 0);
    return result;
  }
}
