package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class PlayerJoined extends Packet {

  private final String nickname;

  public PlayerJoined(Player player) {
    super(Type.PLAYER_JOINED);

    this.nickname = player.getNickname();
  }

  public String getNickname() {
    return nickname;
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

    PlayerJoined that = (PlayerJoined) o;

    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "PlayerJoined{" +
           "nickname='" + nickname + '\'' +
           '}';
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    return result;
  }
}
