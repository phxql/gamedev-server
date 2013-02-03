package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Verifies an email address.
 */
public class VerifyEmail extends Packet {

  /**
   * Email.
   */
  private final String email;

  /**
   * Token.
   */
  private final String token;

  @JsonCreator
  public VerifyEmail(@JsonProperty("email") String email, @JsonProperty("token") String token) {
    super(Type.VERIFY_EMAIL);
    this.email = email;
    this.token = token;
  }

  @Override
  public String toString() {
    return "VerifyEmail{" +
           "email='" + email + '\'' +
           ", token='" + token + '\'' +
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

    VerifyEmail that = (VerifyEmail) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
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
    return result;
  }

  public String getEmail() {

    return email;
  }

  public String getToken() {
    return token;
  }
}
