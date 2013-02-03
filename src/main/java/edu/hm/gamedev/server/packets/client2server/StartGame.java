package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class StartGame extends Packet {

  @JsonCreator
  public StartGame() {
    super(Type.START_GAME);
  }
}
