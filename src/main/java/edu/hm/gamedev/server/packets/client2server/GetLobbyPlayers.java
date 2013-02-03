package edu.hm.gamedev.server.packets.client2server;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class GetLobbyPlayers extends Packet {

  public GetLobbyPlayers() {
    super(Type.GET_LOBBY_PLAYERS);
  }
}
