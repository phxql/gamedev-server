package edu.hm.gamedev.server.packets.exceptions;

public class UnknownPacketTypeException extends PacketDeserializationException {

  private String type;

  public UnknownPacketTypeException(String serialized, String type) {
    super(serialized);
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
