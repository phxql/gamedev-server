package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.MessageBuffer;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.BufferedMessage;
import edu.hm.gamedev.server.packets.client2server.ClientMessage;
import edu.hm.gamedev.server.packets.server2client.BufferedMessageWithSender;
import edu.hm.gamedev.server.packets.server2client.BufferedMessages;
import edu.hm.gamedev.server.packets.server2client.ClientMessageWithSender;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(MessageController.class);

  /**
   * Player collection.
   */
  private final Players players;

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Executor to send buffered messages.
   */
  private ScheduledExecutorService bufferExecutor;

  @Inject
  public MessageController(Players players, CommunicationService communicationService) {
    this.players = players;
    this.communicationService = communicationService;
  }

  /**
   * This method is executed, when the buffer executor ticks.
   */
  private void onBufferExecutorTick() {
    Map<Player, List<BufferedMessageWithSender>>
        packetsToSend = new HashMap<Player, List<BufferedMessageWithSender>>();

    // Iterate over every player
    for (Player player : this.players) {
      if (player.hasGame()) {
        Game game = player.getGame();

        MessageBuffer messageBuffer = game.getMessageBuffer();

        // Get the last buffered message and clear the message for that player
        BufferedMessage bufferedMessage = messageBuffer.getMessageForPlayer(player);
        messageBuffer.clearMessageForPlayer(player);

        if (bufferedMessage != null) {
          // Get the takers from the players game
          for (Player taker : game.getTakers()) {
            // Get the packet list for that taker
            List<BufferedMessageWithSender> packetsForTaker = packetsToSend.get(taker);
            if (packetsForTaker == null) {
              // There is no packet list, create one
              packetsForTaker = new ArrayList<BufferedMessageWithSender>();
            }

            // Add the buffered message from player to the takers send list
            packetsForTaker.add(new BufferedMessageWithSender(bufferedMessage, player));
            packetsToSend.put(taker, packetsForTaker);
          }
        }
      }
    }

    // Now send the accumulated messages to every player in the map
    for (Map.Entry<Player, List<BufferedMessageWithSender>> entry : packetsToSend.entrySet()) {
      communicationService.unicast(entry.getKey(), new BufferedMessages(entry.getValue()));
    }

    logger.trace("Buffer executor executed");
  }

  @Override
  public void init() {
    logger.debug("Starting buffer executor...");

    this.bufferExecutor = Executors.newSingleThreadScheduledExecutor();
    this.bufferExecutor.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        onBufferExecutorTick();
      }
    }, Settings.BufferedMessages.DELAY_MS, Settings.BufferedMessages.DELAY_MS, TimeUnit.MILLISECONDS);

    logger.debug("Buffer executor started");
    logger.debug("Initialized");
  }

  @Override
  public void shutdown() {
    logger.debug("Shutting down buffer executor...");

    this.bufferExecutor.shutdown();
    try {
      if (!this.bufferExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
        this.bufferExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      this.bufferExecutor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    logger.debug("Buffer executor shutted down");
    logger.debug("Shutted down");
  }

  @Override
  public boolean onPacketReceived(final Player player, final Packet packet,
                                  final boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == ClientMessage.class) {
      response = handleClientMessagePacket(player, (ClientMessage) packet);
    } else if (packet.getClass() == BufferedMessage.class) {
      response = handleBufferedMessagePacket(player, (BufferedMessage) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles buffered messages.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleBufferedMessagePacket(Player player, BufferedMessage packet) {
    logger.trace("Handling buffered message packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    MessageBuffer messageBuffer = player.getGame().getMessageBuffer();

    BufferedMessage oldMessage = messageBuffer.getMessageForPlayer(player);
    if (oldMessage == null || oldMessage.getTimestamp() <= packet.getTimestamp()) {
      messageBuffer.setMessageForPlayer(player, packet);
    } else {
      logger.debug("Received out of order buffered message packet {} from player {}, dropping it",
          packet, player);
    }

    return null;
  }

  /**
   * Handles client messages.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleClientMessagePacket(Player player, ClientMessage packet) {
    logger.debug("Handling message packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    Players playersToSend;
    if (player.hasGame()) {
      playersToSend = player.getGame().getTakers();
    } else {
      playersToSend = players.findInLobby();
    }

    communicationService.multicast(playersToSend.except(player),
        new ClientMessageWithSender(player, packet));

    return null;
  }
}
