package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class NotLoggedInPacket extends Packet {

  public NotLoggedInPacket() {
    super(Type.NOT_LOGGED_IN);
  }
}
