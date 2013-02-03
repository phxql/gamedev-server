package edu.hm.gamedev.server.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.*;

public class Games implements Iterable<Game> {

  private final Collection<Game> games;

  public Games() {
    games = Collections.synchronizedList(new ArrayList<Game>());
  }

  public Games(Collection<Game> collectionToWrap) {
    games = collectionToWrap;
  }

  public void addGame(Game game) {
    games.add(game);
  }

  /**
   * Removes a game.
   */
  public void removeGame(Game game) {
    games.remove(game);
  }

  /**
   * Finds a game with the given name.
   *
   * @param name Name.
   * @return The game or null, if no game with the given name exists.
   */
  public Game findGameByName(String name) {
    for (Game game : games) {
      if (game.getName().equals(name)) {
        return game;
      }
    }

    return null;
  }

  /**
   * Finds all joinable games.
   *
   * Joinable means that the game is open and not full.
   *
   * @return open games
   */
  public Games findJoinableGames() {
    return new Games(Collections2.filter(games, new Predicate<Game>() {
      @Override
      public boolean apply(Game game) {
        return game.isOpen() && !game.isFull();
      }
    }));
  }

  /**
   * Finds all active games.
   *
   * @return active games
   */
  public Games findActiveGames() {
    return new Games(Collections2.filter(games, new Predicate<Game>() {
      @Override
      public boolean apply(Game game) {
        return game.isStarted();
      }
    }));
  }


  public boolean hasGameByName(String name) {
    return findGameByName(name) != null;
  }

  @Override
  public Iterator<Game> iterator() {
    return games.iterator();
  }
}
