package edu.hm.gamedev.server.packets.dto;

/**
 * A taker.
 */
public class TakerDto {

  /**
   * Nickname.
   */
  private final String nickname;

  /**
   * True if ready.
   */
  private final boolean ready;

  @Override
  public String toString() {
    return "TakerDto{" +
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

    TakerDto takerDto = (TakerDto) o;

    if (ready != takerDto.ready) {
      return false;
    }
    if (nickname != null ? !nickname.equals(takerDto.nickname) : takerDto.nickname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = nickname != null ? nickname.hashCode() : 0;
    result = 31 * result + (ready ? 1 : 0);
    return result;
  }

  public String getNickname() {

    return nickname;
  }

  public boolean isReady() {
    return ready;
  }

  public TakerDto(String nickname, boolean ready) {
    this.nickname = nickname;
    this.ready = ready;
  }
}
