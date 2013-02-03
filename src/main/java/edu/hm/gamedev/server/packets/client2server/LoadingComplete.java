package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Signals the server that the client has completed loading.
 */
public class LoadingComplete extends Packet {
  public LoadingComplete() {
    super(Type.LOADING_COMPLETE);
  }
}
