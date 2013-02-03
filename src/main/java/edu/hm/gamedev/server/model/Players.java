package edu.hm.gamedev.server.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.*;

import edu.hm.gamedev.server.network.Connection;

public class Players implements Iterable<Player> {

  private final Collection<Player> players;

  public Players() {
    players = Collections.synchronizedList(new ArrayList<Player>());
  }

  public Players(Collection<Player> collectionToWrap) {
    players = collectionToWrap;
  }

  public Players(Player... players) {
    this();

    this.players.addAll(Arrays.asList(players));
  }

  /**
   * Returns the first player.
   *
   * @return Player or null, if no first player exists.
   */
  public Player first() {
    if (players.isEmpty()) {
      return null;
    }

    return players.iterator().next();
  }

  /**
   * Adds a player.
   *
   * @param player Player to add.
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * @return True if the game is empty.
   */
  public boolean isEmpty() {
    return players.isEmpty();
  }

  /**
   * Finds a player with the given nickname.
   *
   * @param nickname Nickname to find.
   * @return Player or null, if the player with the given nickname isn't found.
   */
  public Player findPlayerByNickname(String nickname) {
    for (Player player : players) {
      if (player.getNickname() != null && player.getNickname().equals(nickname)) {
        return player;
      }
    }

    return null;
  }

  /**
   * Finds a player with the given connection.
   *
   * @param connection Connection to find.
   * @return Player or null, if the player with the given connection isn't found.
   */
  public Player findPlayerByConnection(Connection connection) {
    for (Player player : players) {
      if (player.getConnection() == connection) {
        return player;
      }
    }

    return null;
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  /**
   * Returns all players, except the given one.
   *
   * @return All players, except the given one.
   */
  public Players except(final Player player) {
    return new Players(Collections2.filter(players, new Predicate<Player>() {
      @Override
      public boolean apply(Player currentPlayer) {
        return !player.equals(currentPlayer);
      }
    }));
  }

  public int size() {
    return players.size();
  }

  /**
   * Returns the players which are authenticated and are in the lobby.
   *
   * @return Players which are in the lobby.
   */
  public Players findInLobby() {
    return new Players(Collections2.filter(players, new Predicate<Player>() {
      @Override
      public boolean apply(Player player) {
        return player.isAuthenticated() && player.isInLobby();
      }
    }));
  }

  /**
   * Returns the players which are authenticated.
   *
   * @return Players which are authenticated.
   */
  public Players findAuthenticated() {
    return new Players(Collections2.filter(players, new Predicate<Player>() {
      @Override
      public boolean apply(Player player) {
        return player.isAuthenticated();
      }
    }));
  }

  @Override
  public Iterator<Player> iterator() {
    return players.iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Players players1 = (Players) o;

    if (players != null ? !players.equals(players1.players) : players1.players != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return players != null ? players.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Players{" +
           "players=" + players +
           '}';
  }
}
