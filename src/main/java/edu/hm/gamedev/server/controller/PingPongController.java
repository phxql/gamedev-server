package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PingPongController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(PingPongController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  @Inject
  public PingPongController(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == Ping.class) {
      response = handlePingPacket(player);
    } else if (packet.getClass() == Pong.class) {
      response = handlePongPacket(player);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles ping.
   *
   * @param player
   * @return
   */
  private Packet handlePingPacket(Player player) {
    logger.debug("Handling ping packet from player {}", player);

    return new Pong();
  }

  /**
   * Handles pong.
   *
   * @param player
   * @return
   */
  private Packet handlePongPacket(Player player) {
    logger.debug("Handling pong packet from player {}", player);

    return null;
  }
}
