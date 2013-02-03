package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class SetPlayerStorageSuccessful extends Packet {

  public SetPlayerStorageSuccessful() {
    super(Type.SET_PLAYER_STORAGE_SUCCESSFUL);
  }
}
