package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.model.Games;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.services.communication.CommunicationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractControllerTest {
  /**
   * Games.
   */
  protected Players players;
  /**
   * Communication service mock.
   */
  protected CommunicationService communicationService;
  /**
   * Controller under test.
   */
  protected AbstractController controller;
  /**
   * Games.
   */
  protected Games games;

  @Before
  public void setUp() throws Exception {
    players = mock(Players.class);
    communicationService = mock(CommunicationService.class);
    games = mock(Games.class);

    setUpController();
  }

  /**
   * Creates an empty players object.
   *
   * @return Empty players.
   */
  protected Players emptyPlayers() {
    return new Players();
  }

  protected abstract void setUpController() throws Exception;

  @After
  public void tearDown() throws Exception {
    players = null;
    communicationService = null;
    games = null;

    tearDownController();
  }

  protected abstract void tearDownController() throws Exception;

  @Test
  public void testOnPacketReceivedFailed() {

    Player player = mock(Player.class);
    Packet packet = mock(Packet.class);
    assertFalse(controller.onPacketReceived(player, packet, false));
  }

  /**
   * Creates an empty iterator.
   *
   * @param <T> Type of the iterator.
   * @return Empty iterator.
   */
  protected <T> Iterator<T> emptyIterator() {
    return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public T next() {
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
