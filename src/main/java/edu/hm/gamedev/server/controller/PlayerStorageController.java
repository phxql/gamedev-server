package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.GetPlayerStorage;
import edu.hm.gamedev.server.packets.client2server.SetPlayerStorage;
import edu.hm.gamedev.server.packets.server2client.SetPlayerStorageSuccessful;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.storage.PlayerStorage;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PlayerStorageController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(PlayerStorageController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Storage for players.
   */
  private final PlayerStorage playerStorage;

  @Inject
  public PlayerStorageController(CommunicationService communicationService, PlayerStorage playerStorage) {
    this.communicationService = communicationService;
    this.playerStorage = playerStorage;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == SetPlayerStorage.class) {
      response = handleSetPlayerStoragePacket(player, (SetPlayerStorage) packet);
    } else if (packet.getClass() == GetPlayerStorage.class) {
      response = handleGetPlayerStoragePacket(player, (GetPlayerStorage) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles loading of player storage.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleGetPlayerStoragePacket(Player player, GetPlayerStorage packet) {
    logger.debug("Handling get player storage packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    JsonNode content = this.playerStorage.loadStorage(player.getEmail(), packet.getGame());
    return new edu.hm.gamedev.server.packets.server2client.PlayerStorage(content);
  }

  /**
   * Handles saving of player storage.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleSetPlayerStoragePacket(Player player, SetPlayerStorage packet) {
    logger.debug("Handling set player storage packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    this.playerStorage.saveStorage(player.getEmail(), packet.getGame(), packet.getContent());

    return new SetPlayerStorageSuccessful();
  }
}
