package edu.hm.gamedev.server.packets.exceptions;

import edu.hm.gamedev.server.packets.Packet;

public class PacketSerializationException extends Exception {

  /**
   * The packet which caused the exception.
   */
  private final Packet packet;

  public PacketSerializationException(Packet packet, Throwable e) {
    super(e);
    this.packet = packet;
  }

  public Packet getPacket() {
    return packet;
  }
}
