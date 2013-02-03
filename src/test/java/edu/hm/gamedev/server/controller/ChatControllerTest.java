package edu.hm.gamedev.server.controller;

import org.junit.Test;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.client2server.ChatMessage;
import edu.hm.gamedev.server.packets.server2client.ChatMessageWithSender;
import edu.hm.gamedev.server.packets.server2client.NotLoggedInPacket;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatControllerTest extends AbstractControllerTest {

  protected void setUpController() throws Exception {

    controller = new ChatController(players, communicationService);
  }

  protected void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testOnPacketReceivedInGame() throws Exception {
    ChatMessage msg = new ChatMessage("msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    when(player.hasGame()).thenReturn(true);
    when(player.getGame()).thenReturn(mock(Game.class));
    when(player.getGame().getTakers()).thenReturn(mock(Players.class));

    controller.onPacketReceived(player, msg, false);

    verify(communicationService).multicast(any(Players.class), isA(ChatMessageWithSender.class));
  }

  @Test
  public void testOnPacketReceivedInLobby() throws Exception {
    ChatMessage msg = new ChatMessage("msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(true);
    when(players.findInLobby()).thenReturn(mock(Players.class));
    controller.onPacketReceived(player, msg, false);

    verify(communicationService).multicast(any(Players.class), isA(ChatMessageWithSender.class));
  }

  @Test
  public void testOnPacketReceivedNotLoggedIn() throws Exception {
    ChatMessage msg = new ChatMessage("msg");
    Player player = mock(Player.class);
    when(player.isAuthenticated()).thenReturn(false);
    controller.onPacketReceived(player, msg, false);
    verify(communicationService).unicast(same(player), eq(new NotLoggedInPacket()));
  }
}
