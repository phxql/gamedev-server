package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class RegisterSuccessful extends Packet {

  private final boolean emailNeedsVerification;

  public RegisterSuccessful(boolean emailNeedsVerification) {
    super(Type.REGISTER_SUCCESSFUL);
    this.emailNeedsVerification = emailNeedsVerification;
  }

  @Override
  public String toString() {
    return "RegisterSuccessful{" +
           "emailNeedsVerification=" + emailNeedsVerification +
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

    RegisterSuccessful that = (RegisterSuccessful) o;

    if (emailNeedsVerification != that.emailNeedsVerification) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (emailNeedsVerification ? 1 : 0);
    return result;
  }

  public boolean isEmailNeedsVerification() {

    return emailNeedsVerification;
  }
}
