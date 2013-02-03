package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

/**
 * Reset password failed.
 */
public class ResetPasswordFailed extends SomethingFailedPacket<ResetPasswordFailed.Reason> {
  public enum Reason {
    DISABLED,
    EMAIL_NOT_FOUND,
    WRONG_NICKNAME,
    TOKEN_DELIVERY_FAILED,
    EMAIL_NOT_VERIFIED
  }

  public ResetPasswordFailed(Reason reason, String message) {
    super(Type.RESET_PASSWORD_FAILED, reason, message);
  }
}
