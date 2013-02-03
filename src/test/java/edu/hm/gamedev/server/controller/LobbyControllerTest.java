package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.client2server.GetLobbyPlayers;
import edu.hm.gamedev.server.packets.server2client.LobbyPlayers;
import org.junit.Test;

import java.util.Iterator;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LobbyControllerTest extends AbstractControllerTest {
  @Override
  protected void setUpController() throws Exception {
    controller = new LobbyController(communicationService, players);
  }

  @Override
  protected void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testGetLobbyPlayers() {
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    when(player.getNickname()).thenReturn("#1");

    Iterator<Player> empty = emptyIterator();
    Players playersInLobby = mock(Players.class);
    when(playersInLobby.iterator()).thenReturn(empty);
    when(players.findInLobby()).thenReturn(playersInLobby);

    controller.onPacketReceived(player, new GetLobbyPlayers(), false);

    verify(communicationService).unicast(eq(player), isA(LobbyPlayers.class));
  }
}
