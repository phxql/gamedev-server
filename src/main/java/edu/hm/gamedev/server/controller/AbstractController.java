package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.api.Controller;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.server2client.NotInGame;
import edu.hm.gamedev.server.packets.server2client.NotLoggedInPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController implements Controller {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(AbstractController.class);

  @Override
  public void onConnected(Player player) {
  }

  @Override
  public void onDisconnected(Player player) {
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    return false;
  }

  @Override
  public void init() {
  }

  @Override
  public void shutdown() {
  }

  protected Packet playerNotLoggedIn(Player player) {
    logger.debug("Player {} tried to use action without authentication", player);

    return new NotLoggedInPacket();
  }

  protected Packet playerNotInGame(Player player) {
    logger.debug("Player {} tried to use action without a game", player);

    return new NotInGame();
  }
}
