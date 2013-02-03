package edu.hm.gamedev.server.packets.mapping;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.Pong;
import edu.hm.gamedev.server.packets.Type;
import edu.hm.gamedev.server.packets.client2server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapping between Type enum and Packet class.
 *
 * This class exists only to break the cycle between the packets, the client2server and the server2client package.
 */
public final class Mapping {
  /**
   * Static class, no instances allowed.
   */
  private Mapping() {}

  /**
   * Mapping between type and packet class.
   */
  private static final Map<Type, Class<? extends Packet>> MAPPING = new HashMap<Type, Class<? extends Packet>>();

  static {
    MAPPING.put(Type.REGISTER, Register.class);
    MAPPING.put(Type.LOGIN, Login.class);
    MAPPING.put(Type.GET_LOBBY_PLAYERS, GetLobbyPlayers.class);
    MAPPING.put(Type.CREATE_GAME, CreateGame.class);
    MAPPING.put(Type.GET_OPEN_GAMES, GetOpenGames.class);
    MAPPING.put(Type.JOIN_GAME, JoinGame.class);
    MAPPING.put(Type.CLIENT_MESSAGE, ClientMessage.class);
    MAPPING.put(Type.CHANGE_READY, ChangeReady.class);
    MAPPING.put(Type.LOG, Log.class);
    MAPPING.put(Type.START_GAME, StartGame.class);
    MAPPING.put(Type.CLOSE_GAME, CloseGame.class);
    MAPPING.put(Type.BUFFERED_MESSAGE, BufferedMessage.class);
    MAPPING.put(Type.PING, Ping.class);
    MAPPING.put(Type.PONG, Pong.class);
    MAPPING.put(Type.LEAVE_GAME, LeaveGame.class);
    MAPPING.put(Type.SET_PLAYER_STORAGE, SetPlayerStorage.class);
    MAPPING.put(Type.GET_PLAYER_STORAGE, GetPlayerStorage.class);
    MAPPING.put(Type.SET_MAP_STORAGE, SetMapStorage.class);
    MAPPING.put(Type.GET_MAP_STORAGE, GetMapStorage.class);
    MAPPING.put(Type.VERIFY_EMAIL, VerifyEmail.class);
    MAPPING.put(Type.RESET_PASSWORD, ResetPassword.class);
    MAPPING.put(Type.SET_NEW_PASSWORD, SetNewPassword.class);
    MAPPING.put(Type.CHAT_MESSAGE, ChatMessage.class);
    MAPPING.put(Type.GET_TAKERS, GetTakers.class);
    MAPPING.put(Type.LOADING_COMPLETE, LoadingComplete.class);
    MAPPING.put(Type.UNREGISTER, Unregister.class);
  }

  /**
   * Gets the packet class for a given type.
   * @param type Type.
   * @return Packet class or null, if no packet class exists for the given type.
   */
  public static Class<? extends Packet> getPacketClassForType(Type type) {
    return MAPPING.get(type);
  }
}
