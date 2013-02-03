package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class CreateGameSuccessful extends Packet {

  public CreateGameSuccessful() {
    super(Type.CREATE_GAME_SUCCESSFUL);
  }
}
