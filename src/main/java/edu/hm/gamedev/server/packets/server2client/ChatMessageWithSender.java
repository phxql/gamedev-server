package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.client2server.ChatMessage;

/**
 * A chat message with a sender.
 */
public class ChatMessageWithSender extends ChatMessage {

  /**
   * Sender.
   */
  private final String sender;

  public ChatMessageWithSender(Player sender, ChatMessage message) {
    super(message.getMessage());

    this.sender = sender.getNickname();
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

    ChatMessageWithSender that = (ChatMessageWithSender) o;

    if (sender != null ? !sender.equals(that.sender) : that.sender != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (sender != null ? sender.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ChatMessageWithSender{" +
           "sender='" + sender + '\'' +
           '}';
  }

  public String getSender() {
    return sender;
  }
}
