package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

public class LoginFailed extends SomethingFailedPacket<LoginFailed.Reason> {

  public enum Reason {
    CREDENTIALS_INVALID,
    ALREADY_LOGGED_IN,
    EMAIL_NOT_VERIFIED,
    MULTIPLE_LOGIN
  }

  public LoginFailed(Reason reason, String message) {
    super(Type.LOGIN_FAILED, reason, message);
  }
}
