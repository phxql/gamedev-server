package edu.hm.gamedev.server.packets.exceptions;

public class PacketDeserializationException extends Exception {

  private String serialized;

  public PacketDeserializationException(String serialized, Throwable cause) {
    super(cause);
    this.serialized = serialized;
  }

  public PacketDeserializationException(String serialized) {
    this(serialized, null);
  }

  public String getSerialized() {
    return serialized;
  }
}
