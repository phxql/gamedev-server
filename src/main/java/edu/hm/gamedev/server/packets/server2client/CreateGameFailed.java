package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Type;

public class CreateGameFailed extends SomethingFailedPacket<CreateGameFailed.Reason> {

  public enum Reason {
    SLOT_COUNT_TOO_LOW,
    NAME_ALREADY_IN_USE,
    ALREADY_PARTICIPATING_IN_GAME
  }

  public CreateGameFailed(Reason reason, String message) {
    super(Type.CREATE_GAME_FAILED, reason, message);
  }
}
