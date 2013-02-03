package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.server2client.PlayerLeft;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ConnectionController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(ConnectionController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Player collection.
   */
  private final Players players;

  @Inject
  public ConnectionController(CommunicationService communicationService, Players players) {
    this.communicationService = communicationService;
    this.players = players;
  }

  @Override
  public void onConnected(Player player) {
    logger.debug("Player {} connected", player);

    players.addPlayer(player);
  }

  @Override
  public void onDisconnected(Player player) {
    logger.debug("Player {} disconnected", player);

    players.removePlayer(player);

    if (player.isAuthenticated()) {
        communicationService.multicast(players.findInLobby(), new PlayerLeft(player));
    }
  }
}
