package edu.hm.gamedev.server.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.hm.gamedev.server.packets.client2server.BufferedMessage;

/**
 * Buffers messages for clients.
 */
public class MessageBuffer {

  /**
   * Message buffer.
   */
  private final Map<Player, BufferedMessage>
      messages = new ConcurrentHashMap<Player, BufferedMessage>();

  /**
   * Sets the message for a player.
   *
   * @param player Player.
   * @param message Message.
   */
  public void setMessageForPlayer(Player player, BufferedMessage message) {
    this.messages.put(player, message);
  }

  /**
   * Clears the message for a given player.
   *
   * @param player Player.
   */
  public void clearMessageForPlayer(Player player) {
    this.messages.remove(player);
  }

  /**
   * Gets the message for a player.
   *
   * @param player Player.
   * @return Message.
   */
  public BufferedMessage getMessageForPlayer(Player player) {
    return this.messages.get(player);
  }

  /**
   * Removes a player from the message buffer.
   *
   * @param player Player.
   */
  public void removePlayer(Player player) {
    this.messages.remove(player);
  }
}
