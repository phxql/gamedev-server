package edu.hm.gamedev.server.packets.exceptions;

import edu.hm.gamedev.server.packets.Type;

public class UnsupportedPacketTypeException extends PacketDeserializationException {

  private Type type;

  public UnsupportedPacketTypeException(String serialized, Type type) {
    super(serialized);
    this.type = type;
  }

  public Type getType() {
    return type;
  }
}
