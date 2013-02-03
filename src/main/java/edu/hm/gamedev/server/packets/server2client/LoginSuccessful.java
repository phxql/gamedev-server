package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class LoginSuccessful extends Packet {

  private final String nickname;
  private final long timestamp;

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

    LoginSuccessful that = (LoginSuccessful) o;

    if (timestamp != that.timestamp) {
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
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }

  public LoginSuccessful(String nickname, long timestamp) {
    super(Type.LOGIN_SUCCESSFUL);

    this.nickname = nickname;
    this.timestamp = timestamp;

  }

  public String getNickname() {
    return nickname;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "LoginSuccessful{" +
           "nickname='" + nickname + '\'' +
           ", timestamp=" + timestamp +
           '}';
  }
}
