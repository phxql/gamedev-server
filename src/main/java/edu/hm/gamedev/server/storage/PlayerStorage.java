package edu.hm.gamedev.server.storage;

import edu.hm.gamedev.server.storage.dto.PlayerDto;
import org.codehaus.jackson.JsonNode;

/**
 * Interface for a player storage.
 */
public interface PlayerStorage {

  /**
   * Inserts a player.
   *
   * @param dto Player DTO.
   */
  void insert(PlayerDto dto);

  /**
   * Updates the email verified from a given player.
   *
   * @param dto Player DTO.
   */
  void verifyEmail(PlayerDto dto);

  /**
   * Finds a player by the nickname.
   *
   * @param nickname Nickname.
   * @return Player DTO or null, if no player with this nickname exists.
   */
  PlayerDto findByNickname(String nickname);

  /**
   * Finds a player by the email.
   *
   * @param email email
   * @return Player DTO or null, if no player with this email exists.
   */
  PlayerDto findByEmail(String email);

  /**
   * Updates the password reset token for a given player.
   *
   * @param player Player.
   * @param token Token. Can be null.
   */
  void updatePasswordResetToken(PlayerDto player, String token);

  /**
   * Loads the storage of a player.
   *
   * @param email Email of the player.
   * @param game  Game.
   * @return Storage content. Can be null.
   */
  JsonNode loadStorage(String email, String game);

  /**
   * Saves the storage of a player.
   *
   * @param email   Email of the player.
   * @param game    Game.
   * @param content Storage content. Can be null.
   */
  void saveStorage(String email, String game, JsonNode content);

  /**
   * Sets the password for a given player.
   *
   * @param player Player.
   * @param password Password.
   */
  void setPassword(PlayerDto player, String password);

  /**
   * Removes a given account.
   *
   * @param player Player to remove.
   */
  void remove(PlayerDto player);
}
