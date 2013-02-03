package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.client2server.LoadingComplete;

/**
 * Notifies the client that another client has finished loading.
 */
public class LoadingCompleteWithSender extends LoadingComplete {

  /**
   * Nickname of the client which finished loading.
   */
  private final String nickname;

  public LoadingCompleteWithSender(Player player) {
    this.nickname = player.getNickname();
  }

  @Override
  public String toString() {
    return "LoadingCompleteWithSender{" +
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

    LoadingCompleteWithSender that = (LoadingCompleteWithSender) o;

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

  public String getNickname() {
    return nickname;
  }
}
