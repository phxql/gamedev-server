package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

/**
 * Unregistering an account has failed.
 */
public class UnregisterFailed extends SomethingFailedPacket<UnregisterFailed.Reason> {
  /**
   * Reason for the failure.
   */
  public enum Reason {
    EMAIL_NOT_FOUND,
    LOGGED_IN,
    WRONG_PASSWORD
  }

  public UnregisterFailed(Reason reason, String message) {
    super(Type.UNREGISTER_FAILED, reason, message);
  }
}
