package edu.hm.gamedev.server.packets.serialization;

import com.google.common.collect.Lists;
import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.BufferedMessage;
import edu.hm.gamedev.server.packets.client2server.ChatMessage;
import edu.hm.gamedev.server.packets.client2server.ClientMessage;
import edu.hm.gamedev.server.packets.exceptions.PacketSerializationException;
import edu.hm.gamedev.server.packets.server2client.*;
import org.codehaus.jackson.node.BooleanNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class PacketSerializerTest {

  private static PacketSerializer packetSerializer;

  @BeforeClass
  public static void setup() {
    packetSerializer = new PacketSerializer();
  }

  @AfterClass
  public static void tearDown() {
    packetSerializer = null;
  }

  @Test
  public void testLobbyPlayersPacket() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");

    Packet packet = new LobbyPlayers(Lists.newArrayList(player1, player2));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"LOBBY_PLAYERS\",\"players\":[\"Player #1\",\"Player #2\"]}";

    assertEquals(expected, actual);
  }

  @Test
  public void testJoinGameSuccessful() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");
    player2.setReady(true);

    Packet packet = new JoinGameSuccessful(Lists.newArrayList(player1, player2));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"JOIN_GAME_SUCCESSFUL\",\"takers\":"
        + "[{\"nickname\":\"Player #1\",\"ready\":false},"
        + "{\"nickname\":\"Player #2\",\"ready\":true}]}";

    assertEquals(expected, actual);
  }

  @Test
  public void testPlayerJoined() throws PacketSerializationException {
    Packet packet = new PlayerJoined(getPlayer("Player #4"));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"PLAYER_JOINED\",\"nickname\":\"Player #4\"}";

    assertEquals(expected, actual);
  }

  @Test
  public void testPlayerLeft() throws PacketSerializationException {
    Packet packet = new PlayerLeft(getPlayer("Player #4"));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"PLAYER_LEFT\",\"nickname\":\"Player #4\"}";

    assertEquals(expected, actual);
  }

  @Test
  public void testClientMessageWithSender() throws PacketSerializationException {
    ClientMessage message = new ClientMessage(1, BooleanNode.valueOf(true),
        "CHAT_MESSAGE");

    Packet packet = new ClientMessageWithSender(getPlayer("Player #1"), message);

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"CLIENT_MESSAGE\",\"timestamp\":1,\"payload\":true,\"contentType\":\"CHAT_MESSAGE\",\"sender\":\"Player #1\"}";

    assertEquals(expected, actual);
  }

  @Test
  public void testBufferedMessageWithSender() throws PacketSerializationException {
    BufferedMessage message = new BufferedMessage(1, BooleanNode.valueOf(true));

    Packet packet = new BufferedMessageWithSender(message, getPlayer("Player #1"));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"BUFFERED_MESSAGE\",\"timestamp\":1,\"payload\":true,\"sender\":\"Player #1\"}";

    assertEquals(expected, actual);
  }

  @Test
  public void testBufferedMessages() throws PacketSerializationException {
    BufferedMessageWithSender message1 = new BufferedMessageWithSender(
        new BufferedMessage(23, BooleanNode.valueOf(true)),
        getPlayer("Player #1")
    );

    BufferedMessageWithSender message2 = new BufferedMessageWithSender(
        new BufferedMessage(42, BooleanNode.valueOf(true)),
        getPlayer("Player #2")
    );

    Packet packet = new BufferedMessages(message1, message2);

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"BUFFERED_MESSAGES\",\"messages\":[{\"type\":\"BUFFERED_MESSAGE\",\"timestamp\":23,\"payload\":true,\"sender\":\"Player #1\"},{\"type\":\"BUFFERED_MESSAGE\",\"timestamp\":42,\"payload\":true,\"sender\":\"Player #2\"}]}";

    assertEquals(expected, actual);
  }

  @Test
  public void testGameCreated() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");

    Game game = new Game(
        "Name #1", "TOY_RACER", player1, new Players(player1, player2), 20,
        BooleanNode.valueOf(true)
    );

    Packet packet = new GameCreated(game);

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"GAME_CREATED\",\"game\":{\"name\":\"Name #1\","
        + "\"type\":\"TOY_RACER\",\"slots\":20,\"takers\":[\"Player #1\",\"Player #2\"]"
        + ",\"info\":true}}";

    assertEquals(expected, actual);
  }

  @Test
  public void testGameDeleted() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");

    Game game = new Game(
        "Name #1", "TOY_RACER", player1, new Players(player1, player2), 20,
        BooleanNode.valueOf(true)
    );

    Packet packet = new GameDeleted(game);

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"GAME_DELETED\",\"game\":\"Name #1\"}";

    assertEquals(expected, actual);
  }

  @Test
  public void testGameClosed() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");

    Game game = new Game(
        "Name #1", "TOY_RACER", player1, new Players(player1, player2), 20,
        BooleanNode.valueOf(true)
    );

    Packet packet = new GameClosed(game);

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"GAME_CLOSED\",\"game\":\"Name #1\"}";

    assertEquals(expected, actual);
  }


  @Test
  public void testOpenGames() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    Player player2 = getPlayer("Player #2");
    Player player3 = getPlayer("Player #3");

    Game game1 = new Game("Name #1", "TOY_RACER", player1, new Players(player1), 20,
        BooleanNode.valueOf(true)
    );
    Game
        game2 =
        new Game("Name #2", "TOY_RACER", player2, new Players(player2, player3), 10,
            BooleanNode.valueOf(false)
        );

    Packet packet = new OpenGames(Lists.newArrayList(game1, game2));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"OPEN_GAMES\",\"games\":[{\"name\":\"Name #1\","
        + "\"type\":\"TOY_RACER\",\"slots\":20,\"takers\":[\"Player #1\"],\"info\":true},"
        + "{\"name\":\"Name #2\",\"type\":\"TOY_RACER\",\"slots\":10,\"takers\":"
        + "[\"Player #2\",\"Player #3\"],\"info\":false}]}";

    assertEquals(expected, actual);
  }

  @Test
  public void testTakers() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");
    player1.setReady(true);
    Player player2 = getPlayer("Player #2");
    Player player3 = getPlayer("Player #3");

    Packet packet = new Takers(Lists.newArrayList(player1, player2, player3));

    String actual = packetSerializer.build(packet);
    String expected = "{\"type\":\"TAKERS\",\"takers\":[{\"nickname\":\"Player #1\",\"ready\":true}," +
        "{\"nickname\":\"Player #2\",\"ready\":false},{\"nickname\":\"Player #3\",\"ready\":false}]}";

    assertEquals(expected, actual);
  }


  @Test
  public void testChatMessage() throws PacketSerializationException {
    Player player1 = getPlayer("Player #1");

    String actual = packetSerializer.build(new ChatMessageWithSender(player1, new ChatMessage("Hello")));
    String expected = "{\"type\":\"CHAT_MESSAGE\",\"message\":\"Hello\",\"sender\":\"Player #1\"}";

    assertEquals(expected, actual);

  }

  private Player getPlayer(String nickname) {
    Player player = new Player(1, mock(Connection.class));
    player.setAuthenticated(true);
    player.setNickname(nickname);
    return player;
  }
}
