package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Sets a new password.
 */
public class SetNewPassword extends Packet {

  /**
   * Email.
   */
  private final String email;

  /**
   * Token.
   */
  private final String token;

  /**
   * New password.
   */
  private final String password;

  @JsonCreator
  public SetNewPassword(@JsonProperty("email") String email, @JsonProperty("token") String token,
                        @JsonProperty("password") String password) {
    super(Type.SET_NEW_PASSWORD);
    this.email = email;
    this.token = token;
    this.password = password;
  }

  @Override
  public String toString() {
    return "SetNewPassword{" +
           "email='" + email + '\'' +
           ", token='" + token + '\'' +
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

    SetNewPassword that = (SetNewPassword) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (password != null ? !password.equals(that.password) : that.password != null) {
      return false;
    }
    if (token != null ? !token.equals(that.token) : that.token != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (token != null ? token.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  public String getEmail() {
    return email;
  }

  public String getToken() {
    return token;
  }

  public String getPassword() {
    return password;
  }
}
