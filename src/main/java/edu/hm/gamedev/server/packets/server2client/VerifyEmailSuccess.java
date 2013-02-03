package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class VerifyEmailSuccess extends Packet {

  public VerifyEmailSuccess() {
    super(Type.VERIFY_EMAIL_SUCCESS);
  }
}
