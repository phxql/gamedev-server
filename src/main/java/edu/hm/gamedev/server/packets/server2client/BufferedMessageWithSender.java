package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.client2server.BufferedMessage;

public class BufferedMessageWithSender extends BufferedMessage {

  private final String sender;

  public BufferedMessageWithSender(BufferedMessage message, Player player) {
    super(message.getTimestamp(), message.getPayload());

    this.sender = player.getNickname();
  }

  @Override
  public String toString() {
    return "BufferedMessageWithSender{" +
           "sender='" + sender + '\'' +
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

    BufferedMessageWithSender that = (BufferedMessageWithSender) o;

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

  public String getSender() {

    return sender;
  }
}
