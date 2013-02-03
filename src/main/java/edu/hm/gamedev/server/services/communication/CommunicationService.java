package edu.hm.gamedev.server.services.communication;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.packets.Packet;

public interface CommunicationService {

  void init();

  void shutdown();

  /**
   * Sends a packet to a player without blocking.
   *
   * @param player Player.
   * @param packet Packet.
   */
  void unicast(Player player, Packet packet);

  /**
   * Sends a packet to a player.
   *
   * @param player Player.
   * @param packet Packet.
   * @param blocking If true, the method blocks until the packet is delivered.
   */
  void unicast(Player player, Packet packet, boolean blocking);

  /**
   * Sends a packet to multiple players without blocking.
   *
   * @param players Players.
   * @param packet Packet.
   */
  void multicast(Iterable<Player> players, Packet packet);

  /**
   * Sends a packet to multiple players.
   *
   * @param players Players.
   * @param packet Packet.
   * @param blocking If true, the method blocks until all packets are delivered.
   */
  void multicast(Iterable<Player> players, Packet packet, boolean blocking);

  void onConnectionOpened(Connection connection);

  void onConnectionClosed(Connection connection);

  void onMessageReceived(Connection connection, String message);

  void registerForPacketReceivedEvents(PacketReceivedEventHandler handler);

  void registerForPlayerConnectedEvents(PlayerConnectedEventHandler handler);

  void registerForPlayerDisconnectedEvents(PlayerDisconnectedEventHandler handler);

  void unregisterForPacketReceivedEvents(PacketReceivedEventHandler handler);

  void unregisterForPlayerConnectedEvents(PlayerConnectedEventHandler handler);

  void unregisterForPlayerDisconnectedEvents(PlayerDisconnectedEventHandler handler);


  interface PacketReceivedEventHandler {

    /**
     * This callback is executed when a packet is received.
     *
     * @param player The player who send this packet.
     * @param packet The packet itself.
     * @return Was this packet processed by this callback?
     */
    boolean onPacketReceived(Player player, Packet packet, boolean processed);
  }

  interface PlayerConnectedEventHandler {

    void onConnected(Player player);
  }

  interface PlayerDisconnectedEventHandler {

    void onDisconnected(Player player);
  }
}
