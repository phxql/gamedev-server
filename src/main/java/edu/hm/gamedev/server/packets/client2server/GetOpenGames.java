package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class GetOpenGames extends Packet {

  public GetOpenGames() {
    super(Type.GET_OPEN_GAMES);
  }
}
