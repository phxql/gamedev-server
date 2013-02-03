package edu.hm.gamedev.server.packets.server2client;

import java.util.Arrays;
import java.util.Collection;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class BufferedMessages extends Packet {

  private final Collection<BufferedMessageWithSender> messages;

  public BufferedMessages(BufferedMessageWithSender... messages) {
    this(Arrays.asList(messages));
  }

  public BufferedMessages(Collection<BufferedMessageWithSender> messages) {
    super(Type.BUFFERED_MESSAGES);
    this.messages = messages;
  }

  public Collection<BufferedMessageWithSender> getMessages() {
    return messages;
  }

  @Override
  public String toString() {
    return "BufferedMessages{" +
           "messages=" + messages +
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

    BufferedMessages that = (BufferedMessages) o;

    if (messages != null ? !messages.equals(that.messages) : that.messages != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (messages != null ? messages.hashCode() : 0);
    return result;
  }
}
