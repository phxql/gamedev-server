package edu.hm.gamedev.server.controller;

import javax.inject.Inject;

import edu.hm.gamedev.server.Version;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.packets.server2client.Hello;
import edu.hm.gamedev.server.services.communication.CommunicationService;

/**
 * Sends a hello.
 */
public class HelloController extends AbstractController {

  /**
   * Communication service to communicate with players.
   */
  private final CommunicationService communicationService;

  @Inject
  public HelloController(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public void onConnected(Player player) {
    communicationService.unicast(player, new Hello(Version.getBuildNumber(),
                                                   Version.getBuildDate(),
                                                   Version.getBuildRevision()));
  }
}
