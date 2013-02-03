package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.GetMapStorage;
import edu.hm.gamedev.server.packets.client2server.SetMapStorage;
import edu.hm.gamedev.server.packets.server2client.SetMapStorageSuccessful;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.storage.MapStorage;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MapStorageController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(MapStorageController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Storage for maps.
   */
  private final MapStorage mapStorage;

  @Inject
  public MapStorageController(CommunicationService communicationService, MapStorage mapStorage) {
    this.communicationService = communicationService;
    this.mapStorage = mapStorage;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == GetMapStorage.class) {
      response = handleGetMapStoragePacket(player, (GetMapStorage) packet);
    } else if (packet.getClass() == SetMapStorage.class) {
      response = handleSetMapStoragePacket(player, (SetMapStorage) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles loading of map storage.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleGetMapStoragePacket(Player player, GetMapStorage packet) {
    logger.debug("Handling get map storage packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    JsonNode content = this.mapStorage.load(packet.getGame(), packet.getMap());

    return new edu.hm.gamedev.server.packets.server2client.MapStorage(content);
  }

  /**
   * Handles saving of map storage.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleSetMapStoragePacket(Player player, SetMapStorage packet) {
    logger.debug("Handling set map storage packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    this.mapStorage.save(packet.getGame(), packet.getMap(), packet.getContent());

    return new SetMapStorageSuccessful();
  }
}
