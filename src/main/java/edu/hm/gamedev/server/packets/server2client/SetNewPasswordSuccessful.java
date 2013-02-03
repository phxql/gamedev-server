package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Set new password successful.
 */
public class SetNewPasswordSuccessful extends Packet {
  public SetNewPasswordSuccessful() {
    super(Type.SET_NEW_PASSWORD_SUCCESSFUL);
  }
}
