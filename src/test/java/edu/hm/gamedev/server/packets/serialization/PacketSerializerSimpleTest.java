package edu.hm.gamedev.server.packets.serialization;

import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.Pong;
import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.packets.exceptions.PacketSerializationException;
import edu.hm.gamedev.server.packets.server2client.*;
import org.codehaus.jackson.node.BooleanNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(value = Parameterized.class)
public class PacketSerializerSimpleTest {

  private static PacketSerializer packetSerializer;

  private Packet packet;
  private String expected;

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][]{
        {new Login("email", "password"),
            "{\"email\":\"email\",\"password\":\"password\",\"type\":\"LOGIN\"}"},
        {new Register("nickname", "email", "password"),
            "{\"nickname\":\"nickname\",\"email\":\"email\",\"password\":\"password\",\"type\":\"REGISTER\"}"},
        {new LoginFailed(LoginFailed.Reason.ALREADY_LOGGED_IN, "blafoo"),
            "{\"type\":\"LOGIN_FAILED\",\"reason\":\"ALREADY_LOGGED_IN\",\"message\":\"blafoo\"}"},
        {new LoginSuccessful("nickname", 42),
            "{\"type\":\"LOGIN_SUCCESSFUL\",\"nickname\":\"nickname\",\"timestamp\":42}"},
        {new GetLobbyPlayers(), "{\"type\":\"GET_LOBBY_PLAYERS\"}"},
        {new NotLoggedInPacket(), "{\"type\":\"NOT_LOGGED_IN\"}"},
        {new RegisterFailed(RegisterFailed.Reason.NICKNAME_ALREADY_IN_USE, "blafoo"),
            "{\"type\":\"REGISTER_FAILED\",\"reason\":\"NICKNAME_ALREADY_IN_USE\",\"message\":\"blafoo\"}"},
        {new RegisterSuccessful(true), "{\"type\":\"REGISTER_SUCCESSFUL\",\"emailNeedsVerification\":true}"},
        {new CreateGame("blafoo", "TOY_RACER", 20, BooleanNode.valueOf(true)),
            "{\"name\":\"blafoo\",\"gameType\":\"TOY_RACER\",\"slots\":20,\"info\":true,\"type\":\"CREATE_GAME\"}"},
        {new CreateGameFailed(CreateGameFailed.Reason.SLOT_COUNT_TOO_LOW, "Foobar"),
            "{\"type\":\"CREATE_GAME_FAILED\",\"reason\":\"SLOT_COUNT_TOO_LOW\",\"message\":\"Foobar\"}"},
        {new CreateGameSuccessful(), "{\"type\":\"CREATE_GAME_SUCCESSFUL\"}"},
        {new GetOpenGames(), "{\"type\":\"GET_OPEN_GAMES\"}"},
        {new JoinGame("foobar"), "{\"gameName\":\"foobar\",\"type\":\"JOIN_GAME\"}"},
        {new JoinGameFailed(JoinGameFailed.Reason.GAME_NOT_OPEN, "Game is closed"),
            "{\"type\":\"JOIN_GAME_FAILED\",\"reason\":\"GAME_NOT_OPEN\",\"message\":\"Game is closed\"}"},
        {new ClientMessage(1, BooleanNode.valueOf(true), "GAME_DATA"),
            "{\"timestamp\":1,\"payload\":true,\"contentType\":\"GAME_DATA\",\"type\":\"CLIENT_MESSAGE\"}"},
        {new ChangeReady(true), "{\"ready\":true,\"type\":\"CHANGE_READY\"}"},
        {new ReadyChanged("Player #1", true),
            "{\"type\":\"READY_CHANGED\",\"nickname\":\"Player #1\",\"ready\":true}"},
        {new StartGame(), "{\"type\":\"START_GAME\"}"},
        {new StartGameFailed(StartGameFailed.Reason.NOT_HOST, "You are not the host"),
            "{\"type\":\"START_GAME_FAILED\",\"reason\":\"NOT_HOST\",\"message\":\"You are not the host\"}"},
        {new GameStarted(), "{\"type\":\"GAME_STARTED\"}"},
        {new NotInGame(), "{\"type\":\"NOT_IN_GAME\"}"},
        {new CloseGameFailed(CloseGameFailed.Reason.NOT_GAME_CREATOR, "You are not the host"),
            "{\"type\":\"CLOSE_GAME_FAILED\",\"reason\":\"NOT_GAME_CREATOR\",\"message\":\"You are not the host\"}"},
        {new BufferedMessage(42, BooleanNode.valueOf(true)), "{\"timestamp\":42,\"payload\":true,\"type\":\"BUFFERED_MESSAGE\"}"},
        {new Ping(), "{\"type\":\"PING\"}"},
        {new Pong(), "{\"type\":\"PONG\"}"},
        {new LeaveGame(), "{\"type\":\"LEAVE_GAME\"}"},
        {new LeaveGameSuccessful(), "{\"type\":\"LEAVE_GAME_SUCCESSFUL\"}"},
        {new CloseGame(), "{\"type\":\"CLOSE_GAME\"}"},
        {new SetPlayerStorage(BooleanNode.valueOf(true), "GAME"), "{\"content\":true,\"game\":\"GAME\",\"type\":\"SET_PLAYER_STORAGE\"}"},
        {new GetPlayerStorage("GAME"), "{\"game\":\"GAME\",\"type\":\"GET_PLAYER_STORAGE\"}"},
        {new PlayerStorage(BooleanNode.valueOf(true)), "{\"type\":\"PLAYER_STORAGE\",\"content\":true}"},
        {new SetPlayerStorageSuccessful(), "{\"type\":\"SET_PLAYER_STORAGE_SUCCESSFUL\"}"},
        {new Log(1, "Log Message"), "{\"level\":1,\"message\":\"Log Message\",\"type\":\"LOG\"}"},
        {new LogSuccessful(), "{\"type\":\"LOG_SUCCESSFUL\"}"},
        {new GetMapStorage("GAME", "MAP"), "{\"game\":\"GAME\",\"map\":\"MAP\",\"type\":\"GET_MAP_STORAGE\"}"},
        {new SetMapStorage("GAME", "MAP", BooleanNode.valueOf(true)), "{\"game\":\"GAME\",\"map\":\"MAP\",\"content\":true,\"type\":\"SET_MAP_STORAGE\"}"},
        {new MapStorage(BooleanNode.valueOf(true)), "{\"type\":\"MAP_STORAGE\",\"content\":true}"},
        {new SetMapStorageSuccessful(), "{\"type\":\"SET_MAP_STORAGE_SUCCESSFUL\"}"},
        {new VerifyEmailFailed(VerifyEmailFailed.Reason.INVALID_TOKEN, "Invalid token"), "{\"type\":\"VERIFY_EMAIL_FAILED\",\"reason\":\"INVALID_TOKEN\",\"message\":\"Invalid token\"}"},
        {new VerifyEmailSuccess(), "{\"type\":\"VERIFY_EMAIL_SUCCESS\"}"},
        {new VerifyEmail("player1@hm.edu", "12345"), "{\"email\":\"player1@hm.edu\",\"token\":\"12345\",\"type\":\"VERIFY_EMAIL\"}"},
        {new InvalidPacket("foobar"), "{\"type\":\"INVALID_PACKET\",\"packet\":\"foobar\"}"},
        {new ResetPassword("player1@hm.edu", "Player 1"), "{\"email\":\"player1@hm.edu\",\"nickname\":\"Player 1\",\"type\":\"RESET_PASSWORD\"}"},
        {new ResetPasswordSuccessful(), "{\"type\":\"RESET_PASSWORD_SUCCESSFUL\"}"},
        {new ResetPasswordFailed(ResetPasswordFailed.Reason.EMAIL_NOT_FOUND, "Email not found"), "{\"type\":\"RESET_PASSWORD_FAILED\",\"reason\":\"EMAIL_NOT_FOUND\",\"message\":\"Email not found\"}"},
        {new SetNewPassword("player1@hm.edu", "12345", "new-password"), "{\"email\":\"player1@hm.edu\",\"token\":\"12345\",\"password\":\"new-password\",\"type\":\"SET_NEW_PASSWORD\"}"},
        {new SetNewPasswordFailed(SetNewPasswordFailed.Reason.INVALID_TOKEN, "Invalid token"), "{\"type\":\"SET_NEW_PASSWORD_FAILED\",\"reason\":\"INVALID_TOKEN\",\"message\":\"Invalid token\"}"},
        {new SetNewPasswordSuccessful(), "{\"type\":\"SET_NEW_PASSWORD_SUCCESSFUL\"}"},
        {new Hello("#1","1970-01-01_00:00","1"), "{\"type\":\"HELLO\",\"buildNumber\":\"#1\",\"buildDate\":\"1970-01-01_00:00\",\"buildRev\":\"1\"}"},
        {new ChatMessage("hello"), "{\"message\":\"hello\",\"type\":\"CHAT_MESSAGE\"}"},
        {new GetTakers(), "{\"type\":\"GET_TAKERS\"}"},
        {new InternalServerError("foobar"), "{\"type\":\"INTERNAL_SERVER_ERROR\",\"details\":\"foobar\"}"},
        {new LoadingComplete(), "{\"type\":\"LOADING_COMPLETE\"}"},
        {new LoadingCompleteWithSender(getPlayer("Player #1")), "{\"type\":\"LOADING_COMPLETE\",\"nickname\":\"Player #1\"}"},
        {new AllLoadingComplete(), "{\"type\":\"ALL_LOADING_COMPLETE\"}"},
        {new PromotedToHost(), "{\"type\":\"PROMOTED_TO_HOST\"}"},
        {new HostChanged(getPlayer("Player #2")), "{\"type\":\"HOST_CHANGED\",\"newHost\":\"Player #2\"}"},
        {new Unregister("foo@foo.bar", "password"), "{\"email\":\"foo@foo.bar\",\"password\":\"password\",\"type\":\"UNREGISTER\"}"},
        {new UnregisterFailed(UnregisterFailed.Reason.EMAIL_NOT_FOUND, "Email not found"), "{\"type\":\"UNREGISTER_FAILED\",\"reason\":\"EMAIL_NOT_FOUND\",\"message\":\"Email not found\"}"},
        {new UnregisterSuccessful(), "{\"type\":\"UNREGISTER_SUCCESSFUL\"}"},
    };
    return Arrays.asList(data);
  }

  public PacketSerializerSimpleTest(Packet packet, String expected) {
    this.packet = packet;
    this.expected = expected;
  }

  /**
   * Creates a player mock.
   *
   * @param nickname Nickname of the player.
   * @return Player.
   */
  private static Player getPlayer(String nickname) {
    Player player = new Player(1, mock(Connection.class));
    player.setAuthenticated(true);
    player.setNickname(nickname);
    return player;
  }

  @BeforeClass
  public static void setup() {
    packetSerializer = new PacketSerializer();
  }

  @AfterClass
  public static void tearDown() {
    packetSerializer = null;
  }

  @Test
  public void testPacket() throws PacketSerializationException {

    String actual = packetSerializer.build(packet);
    assertEquals(expected, actual);
  }
}
