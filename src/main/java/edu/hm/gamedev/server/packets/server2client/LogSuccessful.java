package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Is sent to the server when the log entry has been successfully inserted.
 */
public class LogSuccessful extends Packet {

  public LogSuccessful() {
    super(Type.LOG_SUCCESSFUL);
  }
}
