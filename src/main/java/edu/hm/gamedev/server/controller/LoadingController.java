package edu.hm.gamedev.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.LoadingComplete;
import edu.hm.gamedev.server.packets.server2client.AllLoadingComplete;
import edu.hm.gamedev.server.packets.server2client.LoadingCompleteWithSender;
import edu.hm.gamedev.server.services.communication.CommunicationService;

/**
 * Controller which handles loading signalling.
 */
public class LoadingController extends AbstractController {

  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(LoadingController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  @Inject
  public LoadingController(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == LoadingComplete.class) {
      response = handleLoadingCompletePacket(player);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles loading complete packet from a client.
   */
  private Packet handleLoadingCompletePacket(Player player) {
    logger.debug("Handling loading complete packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    player.setLoadingComplete(true);

    Game game = player.getGame();
    communicationService
        .multicast(game.getTakers().except(player), new LoadingCompleteWithSender(player));

    boolean allLoadingComplete = true;
    for (Player taker : game.getTakers()) {
      if (!taker.isLoadingComplete()) {
        allLoadingComplete = false;
        break;
      }
    }

    if (allLoadingComplete) {
      logger.debug("All players have finished loading");
      communicationService.multicast(game.getTakers(), new AllLoadingComplete());
    }

    return null;
  }
}
