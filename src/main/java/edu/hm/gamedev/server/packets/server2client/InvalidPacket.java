package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Is sent to the client if the server doesn't understand the packet.
 */
public class InvalidPacket extends Packet {

  /**
   * Packet, which is invalid.
   */
  private final String packet;

  public InvalidPacket(String packet) {
    super(Type.INVALID_PACKET);
    this.packet = packet;
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

    InvalidPacket that = (InvalidPacket) o;

    if (packet != null ? !packet.equals(that.packet) : that.packet != null) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "InvalidPacket{" +
           "packet='" + packet + '\'' +
           '}';
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (packet != null ? packet.hashCode() : 0);
    return result;
  }

  public String getPacket() {
    return packet;
  }
}
