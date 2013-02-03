package edu.hm.gamedev.server.controller.game;

import edu.hm.gamedev.server.api.Controller;
import edu.hm.gamedev.server.controller.AbstractController;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Packet;

/**
 * A controller which only handles events for a specific game.
 */
public class SpecificGameController extends AbstractController {
  /**
   * Only events for this type of game are handled.
   */
  private final String gameType;

  /**
   * Controller to wrap.
   */
  private final Controller toWrap;

  public SpecificGameController(String gameType, Controller toWrap) {
    this.gameType = gameType;
    this.toWrap = toWrap;
  }

  /**
   * Checks if the players game matches the game type.
   *
   * @param player Player.
   * @return True if the players game matches the game type.
   */
  private boolean gameMatches(Player player) {
    return player.hasGame() && player.getGame().getType().equals(this.gameType);
  }

  @Override
  public void onDisconnected(Player player) {
    if (gameMatches(player)) {
      toWrap.onDisconnected(player);
    }
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    if (gameMatches(player)) {
      return toWrap.onPacketReceived(player, packet, processed);
    }

    return false;
  }
}
