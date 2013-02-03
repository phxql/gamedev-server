package edu.hm.gamedev.server.packets.server2client;


import edu.hm.gamedev.server.packets.Type;

public class CloseGameFailed extends SomethingFailedPacket<CloseGameFailed.Reason> {

  public enum Reason {
    NOT_GAME_CREATOR,
    GAME_NOT_STARTED,
    GAME_ALREADY_CLOSED,
  }

  public CloseGameFailed(Reason reason, String message) {
    super(Type.CLOSE_GAME_FAILED, reason, message);
  }
}
