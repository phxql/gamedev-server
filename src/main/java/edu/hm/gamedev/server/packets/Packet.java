package edu.hm.gamedev.server.packets;

/**
 * Abstract class for every packet.
 */
public abstract class Packet {

  /**
   * Type of the packet. Specifies which type the packet has.
   */
  private final Type type;

  public Packet(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Packet packet = (Packet) o;

    if (type != packet.type) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return type != null ? type.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Packet{" +
           "type=" + type +
           '}';
  }
}
