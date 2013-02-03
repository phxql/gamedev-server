package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Packet to get all takers.
 */
public class GetTakers extends Packet {
  public GetTakers() {
    super(Type.GET_TAKERS);
  }
}
