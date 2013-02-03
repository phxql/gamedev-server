package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Resets a password.
 */
public class ResetPassword extends Packet {

  /**
   * Email of the account, which password should be reset.
   */
  private final String email;

  /**
   * Nickname of the account, which password should be reset.
   */
  private final String nickname;

  @JsonCreator
  public ResetPassword(@JsonProperty("email") String email,
                       @JsonProperty("nickname") String nickname) {
    super(Type.RESET_PASSWORD);
    this.email = email;
    this.nickname = nickname;
  }

  @Override
  public String toString() {
    return "ResetPassword{" +
           "email='" + email + '\'' +
           ", nickname='" + nickname + '\'' +
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

    ResetPassword that = (ResetPassword) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    return result;
  }

  public String getEmail() {
    return email;
  }

  public String getNickname() {
    return nickname;
  }
}
