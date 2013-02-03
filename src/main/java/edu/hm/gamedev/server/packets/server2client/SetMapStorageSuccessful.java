package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class SetMapStorageSuccessful extends Packet {

  public SetMapStorageSuccessful() {
    super(Type.SET_MAP_STORAGE_SUCCESSFUL);
  }
}
