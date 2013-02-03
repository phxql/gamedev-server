package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Packet to unregister an account from the server.
 */
public class Unregister extends Packet {
  /**
   * Email address of the account.
   */
  private final String email;

  /**
   * Password of the account.
   */
  private final String password;

  @JsonCreator
  public Unregister(@JsonProperty("email") String email, @JsonProperty("password") String password) {
    super(Type.UNREGISTER);
    this.email = email;
    this.password = password;
  }

  @Override
  public String toString() {
    return "Unregister{" +
        "email='" + email + '\'' +
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

    Unregister that = (Unregister) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
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
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  public String getEmail() {

    return email;
  }

  public String getPassword() {
    return password;
  }
}
