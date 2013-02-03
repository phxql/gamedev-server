package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

public class StartGameFailed extends SomethingFailedPacket<StartGameFailed.Reason> {

  public enum Reason {
    ALREADY_STARTED, NOT_HOST
  }

  public StartGameFailed(Reason reason, String message) {
    super(Type.START_GAME_FAILED, reason, message);
  }
}
