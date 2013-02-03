package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * A chat message.
 */
public class ChatMessage extends Packet {

  /**
   * Message.
   */
  private final String message;

  @JsonCreator
  public ChatMessage(@JsonProperty("message") String message) {
    super(Type.CHAT_MESSAGE);
    this.message = message;
  }

  @Override
  public String toString() {
    return "ChatMessage{" +
           "message='" + message + '\'' +
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

    ChatMessage that = (ChatMessage) o;

    if (message != null ? !message.equals(that.message) : that.message != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }

  public String getMessage() {
    return message;
  }
}
