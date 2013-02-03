package edu.hm.gamedev.server.packets.serialization;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.exceptions.PacketSerializationException;

public class PacketSerializer {

  private final ObjectMapper mapper = new ObjectMapper();

  public String build(Packet packet) throws PacketSerializationException {
    try {
      return mapper.writeValueAsString(packet);
    } catch (IOException e) {
      throw new PacketSerializationException(packet, e);
    }
  }
}
