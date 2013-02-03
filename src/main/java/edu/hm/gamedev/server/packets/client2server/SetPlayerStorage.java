package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Packet to set the player storage.
 */
public class SetPlayerStorage extends Packet {

  /**
   * Content, to which the player storage should be set.
   */
  private final JsonNode content;

  /**
   * Game.
   */
  private final String game;

  @JsonCreator
  public SetPlayerStorage(@JsonProperty("content") JsonNode content,
                          @JsonProperty("game") String game) {
    super(Type.SET_PLAYER_STORAGE);
    this.content = content;
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

    SetPlayerStorage that = (SetPlayerStorage) o;

    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }
    if (game != null ? !game.equals(that.game) : that.game != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (content != null ? content.hashCode() : 0);
    result = 31 * result + (game != null ? game.hashCode() : 0);
    return result;
  }

  public String getGame() {
    return game;
  }

  public JsonNode getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "SetPlayerStorage{" +
           "content=" + content +
           ", game='" + game + '\'' +
           '}';
  }

}
