package edu.hm.gamedev.server.packets.serialization;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.Pong;
import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.packets.exceptions.PacketDeserializationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.BooleanNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class PaketDeserializerSimpleTest {

  private static PacketDeserializer packetDeserializer;

  private String json;
  private Packet packet;

  @BeforeClass
  public static void setup() {
    packetDeserializer = new PacketDeserializer();
  }

  @AfterClass
  public static void tearDown() {
    packetDeserializer = null;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][]{
        {new GetLobbyPlayers(), "{\"type\":\"GET_LOBBY_PLAYERS\"}"},
        {new CreateGame("blafoo", "TOY_RACER", 20, BooleanNode.valueOf(true)),
            "{\"name\":\"blafoo\",\"gameType\":\"TOY_RACER\",\"slots\":20,\"type\":\"CREATE_GAME\",\"info\":true}"},
        {new JoinGame("Game #1"), "{\"type\":\"JOIN_GAME\",\"gameName\":\"Game #1\"}"},
        {new GetOpenGames(), "{\"type\":\"GET_OPEN_GAMES\"}"},
        {new ChangeReady(true), "{\"type\":\"CHANGE_READY\", \"ready\":true}"},
        {new Log(1, "Log Message"),
            "{\"type\":\"LOG\", \"level\":1,\"message\":\"Log Message\"}"},
        {new StartGame(), "{\"type\":\"START_GAME\"}"},
        {new CloseGame(), "{\"type\":\"CLOSE_GAME\"}"},
        {new Register("nickname", "email", "password"),
            "{\"nickname\":\"nickname\",\"email\":\"email\",\"password\":\"password\",\"type\":\"REGISTER\"}"},
        {new Login("email", "password"),
            "{\"email\":\"email\",\"password\":\"password\",\"type\":\"LOGIN\"}"},
        {new BufferedMessage(42, BooleanNode.valueOf(true)), "{\"type\":\"BUFFERED_MESSAGE\",\"timestamp\":42,\"payload\":true}"},
        {new Ping(), "{\"type\":\"PING\"}"},
        {new Pong(), "{\"type\":\"PONG\"}"},
        {new LeaveGame(), "{\"type\":\"LEAVE_GAME\"}"},
        {new SetPlayerStorage(BooleanNode.valueOf(true), "GAME"), "{\"type\":\"SET_PLAYER_STORAGE\",\"content\":true,\"game\":\"GAME\"}"},
        {new GetPlayerStorage("GAME"), "{\"type\":\"GET_PLAYER_STORAGE\",\"game\":\"GAME\"}"},
        {new GetMapStorage("GAME", "MAP"), "{\"game\":\"GAME\",\"map\":\"MAP\",\"type\":\"GET_MAP_STORAGE\"}"},
        {new SetMapStorage("GAME", "MAP", BooleanNode.valueOf(true)), "{\"game\":\"GAME\",\"map\":\"MAP\",\"content\":true,\"type\":\"SET_MAP_STORAGE\"}"},
        {new VerifyEmail("player1@hm.edu", "12345"), "{\"email\":\"player1@hm.edu\",\"token\":\"12345\",\"type\":\"VERIFY_EMAIL\"}"},
        {new ResetPassword("player1@hm.edu", "Player 1"), "{\"email\":\"player1@hm.edu\",\"nickname\":\"Player 1\",\"type\":\"RESET_PASSWORD\"}"},
        {new SetNewPassword("player1@hm.edu", "12345", "new-password"), "{\"email\":\"player1@hm.edu\",\"token\":\"12345\",\"password\":\"new-password\",\"type\":\"SET_NEW_PASSWORD\"}"},
        {new ChatMessage("hello"), "{\"type\":\"CHAT_MESSAGE\",\"message\":\"hello\"}"},
        {new GetTakers(), "{\"type\":\"GET_TAKERS\"}"},
        {new LoadingComplete(), "{\"type\":\"LOADING_COMPLETE\"}"},
        {new Unregister("foo@foo.bar", "password"), "{\"type\":\"UNREGISTER\",\"email\":\"foo@foo.bar\",\"password\":\"password\"}"},
    };
    return Arrays.asList(data);
  }

  public PaketDeserializerSimpleTest(Packet packet, String json) {
    this.json = json;
    this.packet = packet;
  }

  @Test
  public void testPacket() throws PacketDeserializationException {

    Packet actual = packetDeserializer.build(json);
    assertEquals(packet, actual);
  }
}
