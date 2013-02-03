package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.Pong;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PingPongControllerTest extends AbstractControllerTest {
  @Override
  protected void setUpController() throws Exception {
    controller = new PingPongController(communicationService);
  }

  @Override
  protected void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testPingPong() {
    Player player = mock(Player.class);

    controller.onPacketReceived(player, new Ping(), false);

    verify(communicationService).unicast(same(player), isA(Pong.class));
  }
}
