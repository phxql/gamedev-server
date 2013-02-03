package edu.hm.gamedev.server.controller;

import org.junit.Test;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.client2server.BufferedMessage;
import edu.hm.gamedev.server.packets.client2server.ClientMessage;
import edu.hm.gamedev.server.packets.server2client.ClientMessageWithSender;
import edu.hm.gamedev.server.packets.server2client.NotInGame;
import edu.hm.gamedev.server.packets.server2client.NotLoggedInPacket;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageControllerTest extends AbstractControllerTest {

  protected void setUpController() throws Exception {

    controller = new MessageController(players, communicationService);
  }


  public void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testOnPacketReceivedInGame() throws Exception {
    ClientMessage msg = new ClientMessage(1L, null, "msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    when(player.hasGame()).thenReturn(true);
    when(player.getGame()).thenReturn(mock(Game.class));
    when(player.getGame().getTakers()).thenReturn(mock(Players.class));

    controller.onPacketReceived(player, msg, false);

    verify(communicationService).multicast((Iterable<Player>) any(), isA(ClientMessageWithSender.class));
  }

  @Test
  public void testOnPacketReceivedInLobby() throws Exception {
    ClientMessage msg = new ClientMessage(1L, null, "msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    when(players.findInLobby()).thenReturn(mock(Players.class));
    controller.onPacketReceived(player, msg, false);
    verify(communicationService).multicast((Iterable<Player>) any(), isA(ClientMessageWithSender.class));

    reset(communicationService);
    BufferedMessage bufMsg = new BufferedMessage(1l, null);
    controller.onPacketReceived(player, bufMsg, false);
    verify(communicationService).unicast(same(player), eq(new NotInGame()));
  }

  @Test
  public void testOnPacketReceivedNotLoggedIn() throws Exception {
    ClientMessage msg = new ClientMessage(1L, null, "msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(false);
    controller.onPacketReceived(player, msg, false);
    verify(communicationService).unicast(same(player), eq(new NotLoggedInPacket()));

    reset(communicationService);
    BufferedMessage bufMsg = new BufferedMessage(1l, null);
    controller.onPacketReceived(player, bufMsg, false);
    verify(communicationService).unicast(same(player), eq(new NotLoggedInPacket()));

  }
}
