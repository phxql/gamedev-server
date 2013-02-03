package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class ReadyChanged extends Packet {

  private final String nickname;

  private final boolean ready;

  public ReadyChanged(Player player) {
    this(player.getNickname(), player.isReady());
  }

  public ReadyChanged(String nickname, boolean ready) {
    super(Type.READY_CHANGED);
    this.nickname = nickname;
    this.ready = ready;
  }

  public String getNickname() {
    return nickname;
  }

  public boolean isReady() {
    return ready;
  }

  @Override
  public String toString() {
    return "ReadyChanged{" +
           "nickname='" + nickname + '\'' +
           ", ready=" + ready +
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

    ReadyChanged that = (ReadyChanged) o;

    if (ready != that.ready) {
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
    result = 31 * result + (ready ? 1 : 0);
    return result;
  }
}
