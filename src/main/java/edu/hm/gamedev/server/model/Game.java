package edu.hm.gamedev.server.model;

import org.codehaus.jackson.JsonNode;

/**
 * A game.
 */
public class Game {
  /**
   * Name.
   */
  private final String name;
  /**
   * Type.
   */
  private final String type;
  /**
   * Takers.
   */
  private final Players takers;
  /**
   * Slots.
   */
  private final int slots;
  /**
   * Information about the game.
   */
  private final JsonNode info;
  /**
   * True if the game is open.
   */
  private boolean open = true;
  /**
   * True if the game has been started.
   */
  private boolean started = false;
  /**
   * Host of the game.
   */
  private Player host;
  /**
   * Message buffer.
   */
  private final MessageBuffer messageBuffer = new MessageBuffer();

  public Game(String name, String type, Player host, Players takers, int slots, JsonNode info) {
    this.name = name;
    this.type = type;
    this.host = host;
    this.takers = takers;
    this.slots = slots;
    this.info = info;
  }

  public MessageBuffer getMessageBuffer() {
    return messageBuffer;
  }

  public JsonNode getInfo() {
    return info;
  }

  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isOpen() {
    return open;
  }

  public void setHost(Player host) {
    this.host = host;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }

  public Players getTakers() {
    return takers;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getSlots() {
    return slots;
  }

  public boolean isFull() {
    return takers.size() >= slots;
  }

  public boolean isEmpty() {
    return takers.isEmpty();
  }

  public Player getHost() {
    return host;
  }

  public boolean isHost(Player player) {
    return host.equals(player);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Game game = (Game) o;

    if (name != null ? !name.equals(game.name) : game.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
