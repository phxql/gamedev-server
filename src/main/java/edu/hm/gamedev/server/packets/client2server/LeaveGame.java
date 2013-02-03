package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class LeaveGame extends Packet {

  public LeaveGame() {
    super(Type.LEAVE_GAME);
  }
}
