package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.GetLobbyPlayers;
import edu.hm.gamedev.server.packets.server2client.LobbyPlayers;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class LobbyController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(LobbyController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Collection of players.
   */
  private final Players players;

  @Inject
  public LobbyController(CommunicationService communicationService, Players players) {
    this.communicationService = communicationService;
    this.players = players;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == GetLobbyPlayers.class) {
      response = handleGetLobbyPlayersPacket(player);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles the request to get the players in the lobby.
   *
   * @param player
   * @return
   */
  private Packet handleGetLobbyPlayersPacket(Player player) {
    logger.debug("Handling get lobby player packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    return new LobbyPlayers(players.findInLobby());
  }
}
