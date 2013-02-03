package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Is sent when the host of a game changes.
 */
public class HostChanged extends Packet {
  /**
   * Nickname of the new host.
   */
  private final String newHost;

  public HostChanged(Player newHost) {
    super(Type.HOST_CHANGED);

    this.newHost = newHost.getNickname();
  }

  @Override
  public String toString() {
    return "HostChanged{" +
        "newHost='" + newHost + '\'' +
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

    HostChanged that = (HostChanged) o;

    if (newHost != null ? !newHost.equals(that.newHost) : that.newHost != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (newHost != null ? newHost.hashCode() : 0);
    return result;
  }

  public String getNewHost() {
    return newHost;
  }
}
