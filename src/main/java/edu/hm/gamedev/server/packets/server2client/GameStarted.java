package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class GameStarted extends Packet {

  public GameStarted() {
    super(Type.GAME_STARTED);
  }
}
