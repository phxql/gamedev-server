package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.packets.server2client.ChatMessageWithSender;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Controller to handle the chat.
 */
public class ChatController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(ChatController.class);
  /**
   * A service to communicate with clients.
   */
  private final CommunicationService communicationService;
  /**
   * Players on the server.
   */
  private final Players players;

  @Inject
  public ChatController(Players players, CommunicationService communicationService) {
    this.communicationService = communicationService;
    this.players = players;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == ChatMessage.class) {
      response = handleChatMessagePacket(player, (ChatMessage) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles a chat message packet.
   *
   * @param player Player.
   * @param packet Packet.
   * @return Response.
   */
  private Packet handleChatMessagePacket(Player player, ChatMessage packet) {
    logger.debug("Handling chat message packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    Players playersToSend;
    if (player.hasGame()) {
      playersToSend = player.getGame().getTakers();
    } else {
      playersToSend = players.findInLobby();
    }

    communicationService.multicast(playersToSend.except(player), new ChatMessageWithSender(player, packet));

    return null;
  }
}
