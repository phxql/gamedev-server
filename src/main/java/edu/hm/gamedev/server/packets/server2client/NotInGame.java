package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class NotInGame extends Packet {

  public NotInGame() {
    super(Type.NOT_IN_GAME);
  }
}
