package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public abstract class SomethingFailedPacket<T extends Enum<T>> extends Packet {

  private final T reason;
  private final String message;

  protected SomethingFailedPacket(Type type, T reason, String message) {
    super(type);

    this.reason = reason;
    this.message = message;
  }

  public T getReason() {
    return reason;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "SomethingFailedPacket{" +
           "reason=" + reason +
           ", message='" + message + '\'' +
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

    SomethingFailedPacket that = (SomethingFailedPacket) o;

    if (message != null ? !message.equals(that.message) : that.message != null) {
      return false;
    }
    if (reason != null ? !reason.equals(that.reason) : that.reason != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (reason != null ? reason.hashCode() : 0);
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }
}
