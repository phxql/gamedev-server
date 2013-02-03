package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Unregistering an account was successful.
 */
public class UnregisterSuccessful extends Packet {
  public UnregisterSuccessful() {
    super(Type.UNREGISTER_SUCCESSFUL);
  }
}
