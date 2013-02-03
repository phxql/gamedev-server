package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

/**
 * Set new password failed.
 */
public class SetNewPasswordFailed extends SomethingFailedPacket<SetNewPasswordFailed.Reason> {
  public enum Reason {
    DISABLED,
    EMAIL_NOT_FOUND,
    INVALID_TOKEN
  }

  public SetNewPasswordFailed(Reason reason, String message) {
    super(Type.SET_NEW_PASSWORD_FAILED, reason, message);
  }
}
