package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Games;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.packets.server2client.*;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class GameManagementController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(GameManagementController.class);

  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Player collection.
   */
  private final Players players;

  /**
   * Games collection.
   */
  private final Games games;

  @Inject
  public GameManagementController(CommunicationService communicationService, Players players, Games games) {
    this.communicationService = communicationService;
    this.players = players;
    this.games = games;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == CreateGame.class) {
      response = handleCreateGamePacket(player, (CreateGame) packet);
    } else if (packet.getClass() == JoinGame.class) {
      response = handleJoinGamePacket(player, (JoinGame) packet);
    } else if (packet.getClass() == LeaveGame.class) {
      response = handleLeaveGamePacket(player);
    } else if (packet.getClass() == StartGame.class) {
      response = handleStartGamePacket(player);
    } else if (packet.getClass() == CloseGame.class) {
      response = handleCloseGamePacket(player);
    } else if (packet.getClass() == GetOpenGames.class) {
      response = handleGetOpenGamesPacket(player);
    } else if (packet.getClass() == ChangeReady.class) {
      response = handleChangeReadyPacket(player, (ChangeReady) packet);
    } else if (packet.getClass() == GetTakers.class) {
      response = handleGetTakersPacket(player);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles the closing of a game.
   *
   * @param player
   * @return
   */
  private Packet handleCloseGamePacket(Player player) {
    logger.debug("Handling close game packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    Game game = player.getGame();

    if (!game.isHost(player)) {
      return new CloseGameFailed(CloseGameFailed.Reason.NOT_GAME_CREATOR,
          "You are not the host of this game.");
    }

    if (!game.isOpen()) {
      return new CloseGameFailed(CloseGameFailed.Reason.GAME_ALREADY_CLOSED,
          "The game is already closed");
    }

    game.setOpen(false);

    Packet toSend = new GameClosed(game);

    communicationService.multicast(players.findInLobby(), toSend);
    communicationService.multicast(game.getTakers(), toSend);
    return null;
  }

  /**
   * Handles client leaving a game.
   *
   * @param player
   * @return
   */
  private Packet handleLeaveGamePacket(Player player) {
    logger.debug("Handling leave game packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    return removePlayerFromGame(player);
  }

  /**
   * Handles get takers for a game.
   *
   * @param player Sender.
   * @return Packet.
   */
  private Packet handleGetTakersPacket(Player player) {
    logger.debug("Handling get takers packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    return new Takers(player.getGame().getTakers());
  }

  /**
   * Removes a player from a game.
   */
  private Packet removePlayerFromGame(Player player) {
    Game game = player.getGame();

    Players takers = game.getTakers();
    takers.removePlayer(player);
    player.setGame(null);
    game.getMessageBuffer().removePlayer(player);

    if (game.isEmpty()) {
      logger.debug("Game {} is now empty, removing it from game list", game.getName());
      this.games.removeGame(game);

      communicationService.multicast(players.findInLobby(), new GameDeleted(game));
    } else {
      communicationService.multicast(takers, new PlayerLeft(player));

      if (game.getHost().equals(player)) {
        // Host has left the game
        Player newHost = game.getTakers().first();
        game.setHost(newHost);

        logger.debug("Host has left the game, new host is {}", newHost);

        communicationService.unicast(newHost, new PromotedToHost());
        communicationService.multicast(takers, new HostChanged(newHost));
      }
    }

    return new LeaveGameSuccessful();
  }

  /**
   * Handles the ready state change of a client.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleChangeReadyPacket(Player player, ChangeReady packet) {
    logger.debug("Handling change ready packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    player.setReady(packet.isReady());

    communicationService.multicast(player.getGame().getTakers().except(player),
        new ReadyChanged(player));

    return null;
  }

  /**
   * Handles when a client wants to join a game.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleJoinGamePacket(Player player, JoinGame packet) {
    logger.debug("Handling join game packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (player.hasGame()) {
      return new JoinGameFailed(JoinGameFailed.Reason.ALREADY_PARTICIPATING_IN_GAME,
          "You are already participating in a game");
    }

    Game game = games.findGameByName(packet.getGameName());
    if (game == null) {
      return new JoinGameFailed(JoinGameFailed.Reason.NO_GAME_WITH_THIS_NAME,
          "There is no game with this name");
    }

    if (!game.isOpen()) {
      return new JoinGameFailed(JoinGameFailed.Reason.GAME_NOT_OPEN, "Game is not open");
    }

    if (game.isFull()) {
      return new JoinGameFailed(JoinGameFailed.Reason.GAME_FULL, "Game is full");
    }

    game.getTakers().addPlayer(player);
    player.setGame(game);
    player.setReady(false);
    player.setLoadingComplete(false);

    // Notify players in game that player joined
    communicationService.multicast(game.getTakers().except(player), new PlayerJoined(player));
    // Notify players in lobby that player left
    communicationService.multicast(players.findInLobby().except(player), new PlayerLeft(player));

    return new JoinGameSuccessful(game.getTakers());
  }

  /**
   * Handles the request for the open games list.
   *
   * @param player
   * @return
   */
  private Packet handleGetOpenGamesPacket(Player player) {
    logger.debug("Handling get open games packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    return new OpenGames(games.findJoinableGames());
  }

  /**
   * Handles creation of a game.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleCreateGamePacket(Player player, CreateGame packet) {
    logger.debug("Handling create game packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (packet.getSlots() < 1) {
      logger.debug("Slot count {} is too low", packet.getSlots());
      return new CreateGameFailed(CreateGameFailed.Reason.SLOT_COUNT_TOO_LOW,
          "Slot count must be one or greater");
    }

    if (player.hasGame()) {
      logger.debug("Player {} already participating in game {}", player, player.getGame());
      return new CreateGameFailed(CreateGameFailed.Reason.ALREADY_PARTICIPATING_IN_GAME,
          "You are already participating in a game");
    }

    if (games.hasGameByName(packet.getName())) {
      logger.debug("Game name {} already in use", packet.getName());
      return new CreateGameFailed(CreateGameFailed.Reason.NAME_ALREADY_IN_USE,
          "Game name already in use");
    }

    logger.info("Player {} created game {} named {}",
        player, packet.getGameType(), packet.getName()
    );

    Players takers = new Players(player);
    Game game = new Game(
        packet.getName(), packet.getGameType(), player, takers, packet.getSlots(), packet.getInfo()
    );

    player.setGame(game);
    player.setReady(true);
    games.addGame(game);

    Players playersToNotify = players.findInLobby().except(player);

    communicationService.multicast(playersToNotify, new PlayerLeft(player));
    communicationService.multicast(playersToNotify, new GameCreated(game));

    return new CreateGameSuccessful();
  }

  /**
   * Handles the start of a game.
   *
   * @param player
   * @return
   */
  private Packet handleStartGamePacket(Player player) {
    logger.debug("Handling start game packet from player {}", player);

    if (!player.isAuthenticated()) {
      return playerNotLoggedIn(player);
    }

    if (!player.hasGame()) {
      return playerNotInGame(player);
    }

    Game game = player.getGame();

    if (!game.isHost(player)) {
      return new StartGameFailed(StartGameFailed.Reason.NOT_HOST,
          "Only the host can start the game");
    }

    if (game.isStarted()) {
      return new StartGameFailed(StartGameFailed.Reason.ALREADY_STARTED,
          "The game is already running");
    }

    game.setStarted(true);

    communicationService.multicast(player.getGame().getTakers(), new GameStarted());

    return null;
  }

  @Override
  public void onDisconnected(Player player) {
    logger.debug("Player {} disconnected", player);
    if (player.isAuthenticated() && player.hasGame()) {
      removePlayerFromGame(player);
    }
  }
}
