package edu.hm.gamedev.server.packets.server2client;

import org.codehaus.jackson.JsonNode;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class PlayerStorage extends Packet {

  private final JsonNode content;

  public PlayerStorage(JsonNode content) {
    super(Type.PLAYER_STORAGE);
    this.content = content;
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

    PlayerStorage that = (PlayerStorage) o;

    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlayerStorage{" +
           "content=" + content +
           '}';
  }

  public JsonNode getContent() {
    return content;
  }
}
