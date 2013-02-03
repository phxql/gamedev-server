package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Promotes a client to the new host.
 */
public class PromotedToHost extends Packet {
  public PromotedToHost() {
    super(Type.PROMOTED_TO_HOST);
  }
}
