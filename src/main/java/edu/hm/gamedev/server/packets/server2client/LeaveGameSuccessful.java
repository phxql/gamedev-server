package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class LeaveGameSuccessful extends Packet {

  public LeaveGameSuccessful() {
    super(Type.LEAVE_GAME_SUCCESSFUL);
  }
}
