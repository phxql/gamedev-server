package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Password reset successful.
 */
public class ResetPasswordSuccessful extends Packet {
  public ResetPasswordSuccessful() {
    super(Type.RESET_PASSWORD_SUCCESSFUL);
  }
}
