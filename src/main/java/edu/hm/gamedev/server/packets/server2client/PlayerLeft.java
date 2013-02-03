package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class PlayerLeft extends Packet {

  private final String nickname;

  public PlayerLeft(Player player) {
    super(Type.PLAYER_LEFT);
    this.nickname = player.getNickname();
  }

  public String getNickname() {
    return nickname;
  }

  @Override
  public String toString() {
    return "PlayerLeft{" +
           "nickname='" + nickname + '\'' +
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

    PlayerLeft that = (PlayerLeft) o;

    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    return result;
  }
}
