package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.packets.client2server.CreateGame;
import edu.hm.gamedev.server.packets.server2client.CreateGameFailed;
import edu.hm.gamedev.server.packets.server2client.GameCreated;
import edu.hm.gamedev.server.packets.server2client.PlayerLeft;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameManagementControllerTest extends AbstractControllerTest {
  @Override
  protected void setUpController() throws Exception {
    controller = new GameManagementController(communicationService, players, games);
  }

  @Override
  protected void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testGameCreatedPacket() {
    Player player = new Player(1, mock(Connection.class));
    player.setAuthenticated(true);
    player.setNickname("Player #1");

    Game game = new Game("Game #1", "TEST", player, new Players(player), 10, null);

    when(players.findInLobby()).thenReturn(emptyPlayers());

    controller.onPacketReceived(player, new CreateGame(game.getName(), game.getType(), game.getSlots(), game.getInfo()), false);

    verify(communicationService).multicast(any(Players.class), eq(new PlayerLeft(player)));
    verify(communicationService).multicast(any(Players.class), eq(new GameCreated(game)));
  }

  @Test
  public void createGameFailsWhenAlreadyInGame() {
    Player player = new Player(1, mock(Connection.class));
    player.setAuthenticated(true);
    player.setNickname("Player #1");

    Game game = new Game("Game #1", "TEST", player, new Players(player), 10, null);

    player.setGame(game);

    controller.onPacketReceived(player, new CreateGame(game.getName(), game.getType(), game.getSlots(), game.getInfo()), false);

    verify(communicationService).unicast(eq(player), eq(new CreateGameFailed(CreateGameFailed.Reason.ALREADY_PARTICIPATING_IN_GAME, "You are already participating in a game")));
  }

  @Test
  public void createGameFailsWhenSlotsLessThanOne() {
    Player player = new Player(1, mock(Connection.class));
    player.setAuthenticated(true);
    player.setNickname("Player #1");

    controller.onPacketReceived(player, new CreateGame("Game #1", "TEST", 0, null), false);

    verify(communicationService).unicast(eq(player), eq(new CreateGameFailed(CreateGameFailed.Reason.SLOT_COUNT_TOO_LOW, "Slot count must be one or greater")));
  }
}
