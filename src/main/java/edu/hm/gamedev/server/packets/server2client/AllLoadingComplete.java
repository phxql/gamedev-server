package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Notifies a client that all clients have finished loading.
 */
public class AllLoadingComplete extends Packet {
  public AllLoadingComplete() {
    super(Type.ALL_LOADING_COMPLETE);
  }
}
