package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.Log;
import edu.hm.gamedev.server.packets.server2client.LogSuccessful;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.services.time.TimeService;
import edu.hm.gamedev.server.storage.ClientLogStorage;
import edu.hm.gamedev.server.storage.dto.LogEntryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Timestamp;

public class LogController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(LogController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Storage for client logs.
   */
  private final ClientLogStorage clientLogStorage;


  @Inject
  public LogController(CommunicationService communicationService, ClientLogStorage clientLogStorage) {
    this.communicationService = communicationService;
    this.clientLogStorage = clientLogStorage;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == Log.class) {
      response = handleLogPacket(player, (Log) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles a log packet.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleLogPacket(Player player, Log packet) {
    logger.debug("Handling log packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    Timestamp now = new Timestamp(TimeService.getInstance().getTicks());

    LogEntryDto logEntry =
        new LogEntryDto(now, packet.getMessage(), packet.getLevel(), player.getEmail());
    this.clientLogStorage.insert(logEntry);

    return new LogSuccessful();
  }
}
