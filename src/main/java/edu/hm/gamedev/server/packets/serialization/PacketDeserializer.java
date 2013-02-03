package edu.hm.gamedev.server.packets.serialization;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;
import edu.hm.gamedev.server.packets.exceptions.PacketDeserializationException;
import edu.hm.gamedev.server.packets.exceptions.UnknownPacketTypeException;
import edu.hm.gamedev.server.packets.exceptions.UnsupportedPacketTypeException;
import edu.hm.gamedev.server.packets.mapping.Mapping;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class PacketDeserializer {

  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Builds a packet from a given JSON document.
   *
   * @param serialized JSON document.
   * @return Deserialized packet.
   * @throws PacketDeserializationException If the packet couldn't be deserialized.
   */
  public Packet build(String serialized) throws PacketDeserializationException {
    JsonNode node;
    try {
      node = mapper.readTree(serialized);
    } catch (IOException e) {
      throw new PacketDeserializationException(serialized, e);
    }

    if (!node.has("type")) {
      throw new PacketDeserializationException(serialized);
    }
    String type = node.get("type").asText();

    for (Type availableType : Type.values()) {
      if (availableType.name().equals(type)) {
        return build(serialized, availableType);
      }
    }

    throw new UnknownPacketTypeException(serialized, type);
  }

  /**
   * Builds a packet from a given JSON document and a given packet type.
   *
   * @param serialized JSON document.
   * @param type       Packet type.
   * @return Built package.
   */
  private Packet build(String serialized, Type type) throws PacketDeserializationException {
    Class<? extends Packet> packetClass = Mapping.getPacketClassForType(type);

    if (packetClass == null) {
      throw new UnsupportedPacketTypeException(serialized, type);
    }

    try {
      return mapper.readValue(serialized, packetClass);
    } catch (IOException e) {
      throw new PacketDeserializationException(serialized, e);
    }
  }
}
