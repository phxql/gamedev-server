package edu.hm.gamedev.server.controller;

import org.junit.Test;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.server2client.Hello;

import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HelloControllerTest extends AbstractControllerTest {

  public void setUpController() throws Exception {
    controller = new HelloController(communicationService);
  }

  public void tearDownController() throws Exception {
    controller = null;
  }

  @Test
  public void testOnConnected() throws Exception {

    Player player = mock(Player.class);
    controller.onConnected(player);

    verify(communicationService).unicast(same(player), isA(Hello.class));
  }
}
