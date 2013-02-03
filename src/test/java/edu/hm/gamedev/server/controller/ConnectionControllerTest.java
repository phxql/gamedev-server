package edu.hm.gamedev.server.controller;

import org.junit.Test;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.server2client.PlayerLeft;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectionControllerTest extends AbstractControllerTest {

  @Override
  protected void setUpController() throws Exception {
    controller = new ConnectionController(communicationService, players);
  }

  @Override
  protected void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testOnConnected() throws Exception {
    Player player = mock(Player.class);
    controller.onConnected(player);
    verify(players).addPlayer(same(player));
  }

  @Test
  public void testOnDisconnectedNotAuthenticated() throws Exception {
    Player player = mock(Player.class);
    controller.onDisconnected(player);
    verify(players).removePlayer(same(player));
  }

  @Test
  public void testOnDisconnectedAuthenticated() throws Exception {
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    controller.onDisconnected(player);
    verify(players).removePlayer(same(player));
    verify(communicationService).multicast((Iterable<Player>) any(), isA(PlayerLeft.class));
  }
}
