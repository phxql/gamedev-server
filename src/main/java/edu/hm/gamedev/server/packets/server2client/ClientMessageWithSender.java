package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.client2server.ClientMessage;

public class ClientMessageWithSender extends ClientMessage {

  private final String sender;

  public ClientMessageWithSender(Player sender, ClientMessage clientMessage) {
    super(clientMessage.getTimestamp(), clientMessage.getPayload(), clientMessage.getContentType());

    this.sender = sender.getNickname();
  }

  public String getSender() {
    return sender;
  }

  @Override
  public String toString() {
    return "ClientMessageWithSender{" +
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

    ClientMessageWithSender that = (ClientMessageWithSender) o;

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
}
