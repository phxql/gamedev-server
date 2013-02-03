package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

public class RegisterFailed extends SomethingFailedPacket<RegisterFailed.Reason> {

  public enum Reason {
    NICKNAME_ALREADY_IN_USE,
    EMAIL_ALREADY_IN_USE,
    TOKEN_DELIVERY_FAILED
  }

  public RegisterFailed(Reason reason, String message) {
    super(Type.REGISTER_FAILED, reason, message);
  }
}
