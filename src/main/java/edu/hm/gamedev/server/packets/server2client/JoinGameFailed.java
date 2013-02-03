package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

public class JoinGameFailed extends SomethingFailedPacket<JoinGameFailed.Reason> {

  public enum Reason {
    ALREADY_PARTICIPATING_IN_GAME,
    NO_GAME_WITH_THIS_NAME,
    GAME_FULL,
    GAME_NOT_OPEN
  }

  public JoinGameFailed(Reason reason, String message) {
    super(Type.JOIN_GAME_FAILED, reason, message);
  }
}
