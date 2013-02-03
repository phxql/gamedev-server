package edu.hm.gamedev.server.storage;

import edu.hm.gamedev.server.storage.dto.PlayerDto;
import org.codehaus.jackson.JsonNode;

import java.util.*;

public class MemoryPlayerStorage implements PlayerStorage {

  private final List<PlayerDto> players = Collections.synchronizedList(new ArrayList<PlayerDto>());
  private final
  Map<String, JsonNode> playerStorage =
      Collections.synchronizedMap(new HashMap<String, JsonNode>());

  @Override
  public void insert(PlayerDto dto) {
    players.add(dto);
  }

  @Override
  public void verifyEmail(PlayerDto dto) {
    // TODO: Implement
  }

  @Override
  public PlayerDto findByNickname(String nickname) {
    for (PlayerDto player : players) {
      if (player.getNickname().equals(nickname)) {
        return player;
      }
    }

    return null;
  }

  @Override
  public PlayerDto findByEmail(String email) {
    for (PlayerDto player : players) {
      if (player.getEmail().equals(email)) {
        return player;
      }
    }

    return null;
  }

  @Override
  public void updatePasswordResetToken(PlayerDto player, String token) {
    // TODO: Implement
  }

  @Override
  public JsonNode loadStorage(String email, String game) {
    return playerStorage.get(email + "." + game);
  }

  @Override
  public void saveStorage(String email, String game, JsonNode content) {
    playerStorage.put(email + "." + game, content);
  }

  @Override
  public void setPassword(PlayerDto player, String password) {
    // TODO: Implement
  }

  @Override
  public void remove(PlayerDto player) {
    // TODO: Remove
  }
}
