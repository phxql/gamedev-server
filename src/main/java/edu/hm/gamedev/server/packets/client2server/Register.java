package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Registers a client with the server.
 */
public class Register extends Packet {

  /**
   * The nickname.
   */
  private final String nickname;

  /**
   * The e-Mail.
   */
  private final String email;

  /**
   * The password.
   */
  private final String password;

  @JsonCreator
  public Register(@JsonProperty("nickname") String nickname, @JsonProperty("email") String email,
                  @JsonProperty("password") String password) {
    super(Type.REGISTER);
    this.nickname = nickname;
    this.email = email;
    this.password = password;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return "Register{" +
           "nickname='" + nickname + '\'' +
           ", email='" + email + '\'' +
           ", password='" + password + '\'' +
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

    Register that = (Register) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }
    if (password != null ? !password.equals(that.password) : that.password != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}
