package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

/**
 * Email verification failed.
 */
public class VerifyEmailFailed extends SomethingFailedPacket<VerifyEmailFailed.Reason> {

  public enum Reason {
    ALREADY_VERIFIED,
    EMAIL_NOT_FOUND,
    DISABLED,
    INVALID_TOKEN
  }

  public VerifyEmailFailed(Reason reason, String message) {
    super(Type.VERIFY_EMAIL_FAILED, reason, message);
  }
}
