package edu.hm.gamedev.server.api;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.services.communication.CommunicationService;

public interface Controller extends CommunicationService.PlayerConnectedEventHandler,
                                    CommunicationService.PacketReceivedEventHandler,
                                    CommunicationService.PlayerDisconnectedEventHandler {

  @Override
  void onConnected(Player player);

  @Override
  void onDisconnected(Player player);

  @Override
  boolean onPacketReceived(Player player, Packet packet, boolean processed);

  void init();

  void shutdown();
}
