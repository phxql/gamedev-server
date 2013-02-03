package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class CloseGame extends Packet {

  @JsonCreator
  public CloseGame() {
    super(Type.CLOSE_GAME);
  }
}
