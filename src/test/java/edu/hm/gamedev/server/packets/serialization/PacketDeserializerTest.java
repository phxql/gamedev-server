package edu.hm.gamedev.server.packets.serialization;

import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.packets.exceptions.PacketDeserializationException;
import edu.hm.gamedev.server.packets.exceptions.UnknownPacketTypeException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.BooleanNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PacketDeserializerTest {

  private static PacketDeserializer packetDeserializer;
  private static ObjectMapper mapper;

  @BeforeClass
  public static void setup() {
    packetDeserializer = new PacketDeserializer();
    mapper = new ObjectMapper();
  }

  @AfterClass
  public static void tearDown() {
    packetDeserializer = null;
    mapper = null;
  }

  @Test(expected = UnknownPacketTypeException.class)
  public void testException() throws PacketDeserializationException {
    packetDeserializer.build("{\"type\":\"DUMMY\"}");
  }

  @Test(expected = PacketDeserializationException.class)
  public void testException2() throws PacketDeserializationException {
    packetDeserializer.build("{\"dummy\":\"\"}");
  }

  @Test(expected = PacketDeserializationException.class)
  public void testException3() throws PacketDeserializationException {
    packetDeserializer.build("No JSON at all");
  }

  @Test
  public void testClientMessage() throws PacketDeserializationException, IOException {
    Packet actual = packetDeserializer.build(
        "{\"type\":\"CLIENT_MESSAGE\",\"timestamp\":1,\"payload\":{\"foobar\":true}, \"contentType\":\"CHAT_MESSAGE\"}"
    );
    Packet expected = new ClientMessage(1, mapper.readTree("{\"foobar\":true}"),
                                        "CHAT_MESSAGE");

    assertEquals(expected, actual);
  }

}
