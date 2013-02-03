package edu.hm.gamedev.server.controller;

import edu.hm.gamedev.server.packets.client2server.*;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.server2client.*;
import edu.hm.gamedev.server.services.messaging.MessageDeliveryFailedException;
import edu.hm.gamedev.server.services.messaging.MessagingService;
import edu.hm.gamedev.server.services.passwordHash.PasswordHashService;
import edu.hm.gamedev.server.services.time.TimeService;
import edu.hm.gamedev.server.services.token.TokenService;
import edu.hm.gamedev.server.settings.Settings;
import edu.hm.gamedev.server.storage.PlayerStorage;
import edu.hm.gamedev.server.storage.dto.PlayerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Handles player authentication.
 */
public class AuthenticationController extends AbstractController {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  /**
   * Player storage.
   */
  private final PlayerStorage playerStorage;
  /**
   * Service to hash passwords.
   */
  private final PasswordHashService passwordHashService;
  /**
   * Service to generate tokens.
   */
  private final TokenService tokenService;
  /**
   * Service to send messages to the user.
   */
  private final MessagingService messagingService;
  /**
   * Service to communicate with players.
   */
  private final CommunicationService communicationService;

  /**
   * Collection of players.
   */
  private final Players players;

  @Inject
  public AuthenticationController(PlayerStorage playerStorage, PasswordHashService passwordHashService, TokenService tokenService, MessagingService messagingService, CommunicationService communicationService, Players players) {
    this.playerStorage = playerStorage;
    this.passwordHashService = passwordHashService;
    this.tokenService = tokenService;
    this.messagingService = messagingService;
    this.communicationService = communicationService;
    this.players = players;
  }

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    logger.trace("Dispatching packet {}", packet);

    Packet response;

    if (packet.getClass() == Register.class) {
      response = handleRegisterPacket(player, (Register) packet);
    } else if (packet.getClass() == VerifyEmail.class) {
      response = handleVerifyEmailPacket(player, (VerifyEmail) packet);
    } else if (packet.getClass() == Login.class) {
      response = handleLoginPacket(player, (Login) packet);
    } else if (packet.getClass() == ResetPassword.class) {
      response = handleResetPasswordPacket(player, (ResetPassword) packet);
    } else if (packet.getClass() == SetNewPassword.class) {
      response = handleSetNewPasswordPacket(player, (SetNewPassword) packet);
    } else if (packet.getClass() == Unregister.class) {
      response = handleUnregisterPacket(player, (Unregister) packet);
    } else {
      return false;
    }

    if (response != null) {
      communicationService.unicast(player, response);
    }

    return true;
  }

  /**
   * Handles player registration.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleRegisterPacket(Player player, Register packet) {
    logger.debug("Handling register packet from player {}", player);

    if (playerStorage.findByNickname(packet.getNickname()) != null) {
      logger.info("Registration for player {} failed, nickname '{}' already in use", player,
          packet.getNickname());

      return new RegisterFailed(RegisterFailed.Reason.NICKNAME_ALREADY_IN_USE,
          "Nickname already in use");
    }

    if (playerStorage.findByEmail(packet.getEmail()) != null) {
      logger.info("Registration for player {} failed, email {} already in use", player,
          packet.getEmail());

      return new RegisterFailed(RegisterFailed.Reason.EMAIL_ALREADY_IN_USE,
          "e-Mail already in use");
    }

    logger.info("Player {} registered as {}", player, packet.getNickname());

    String hashedPassword = passwordHashService.hash(packet.getPassword());
    String verifyToken = Settings.EmailVerification.ENABLED ? tokenService.generate() : "";

    playerStorage.insert(new PlayerDto(packet.getNickname(), packet.getEmail(), hashedPassword,
        !Settings.EmailVerification.ENABLED, verifyToken, null));
    if (Settings.EmailVerification.ENABLED) {
      try {
        messagingService.send(packet.getEmail(), "Your account registration", "To activate your account, use the following token: " + verifyToken);
      } catch (MessageDeliveryFailedException e) {
        logger.debug("Token delivery failed, token is {}", verifyToken);
        return new RegisterFailed(RegisterFailed.Reason.TOKEN_DELIVERY_FAILED, "Token delivery failed");
      }
    }

    return new RegisterSuccessful(Settings.EmailVerification.ENABLED);
  }

  /**
   * Handles the unregistering of an account.
   *
   * @param player Player who sent the packet.
   * @param packet Packet which should be handled.
   * @return Response.
   */
  private Packet handleUnregisterPacket(Player player, Unregister packet) {
    logger.debug("Handling unregister packet from player {}", player);

    if (player.isAuthenticated()) {
      return new UnregisterFailed(UnregisterFailed.Reason.LOGGED_IN, "You can't unregister if you are logged in");
    }

    PlayerDto dto = playerStorage.findByEmail(packet.getEmail());

    // TODO: Timing attacks possible!
    if (dto == null) {
      return new UnregisterFailed(UnregisterFailed.Reason.EMAIL_NOT_FOUND, "Email address not found");
    }

    if (!passwordHashService.verify(packet.getPassword(), dto.getPassword())) {
      return new UnregisterFailed(UnregisterFailed.Reason.WRONG_PASSWORD, "Wrong password");
    }

    playerStorage.remove(dto);

    return new UnregisterSuccessful();
  }

  /**
   * Handles setting of new password.
   *
   * @param player Player.
   * @param packet Packet.
   * @return Response packet. Can be null.
   */
  private Packet handleSetNewPasswordPacket(Player player, SetNewPassword packet) {
    logger.debug("Handling set new password packet from player {}", player);

    if (!Settings.PasswordReset.ENABLED) {
      return new SetNewPasswordFailed(SetNewPasswordFailed.Reason.DISABLED, "Password reset is disabled");
    }

    PlayerDto dto = playerStorage.findByEmail(packet.getEmail());
    if (dto == null) {
      return new SetNewPasswordFailed(SetNewPasswordFailed.Reason.EMAIL_NOT_FOUND, "Email not found");
    }

    if (dto.getResetPasswordToken() == null || !dto.getResetPasswordToken().equals(packet.getToken())) {
      return new SetNewPasswordFailed(SetNewPasswordFailed.Reason.INVALID_TOKEN, "Invalid token");
    }

    String hashedPassword = passwordHashService.hash(packet.getPassword());
    playerStorage.setPassword(dto, hashedPassword);
    playerStorage.updatePasswordResetToken(dto, null);
    playerStorage.verifyEmail(dto);

    return new SetNewPasswordSuccessful();
  }

  /**
   * Handles password reset.
   *
   * @param player Player.
   * @param packet Packet.
   * @return Response packet. Can be null.
   */
  private Packet handleResetPasswordPacket(Player player, ResetPassword packet) {
    logger.debug("Handling reset password packet from player {}", player);

    if (!Settings.PasswordReset.ENABLED) {
      return new ResetPasswordFailed(ResetPasswordFailed.Reason.DISABLED, "Password reset is disabled");
    }

    PlayerDto dto = playerStorage.findByEmail(packet.getEmail());
    if (dto == null) {
      return new ResetPasswordFailed(ResetPasswordFailed.Reason.EMAIL_NOT_FOUND, "Email not found");
    }

    if (!dto.isEmailVerified()) {
      return new ResetPasswordFailed(ResetPasswordFailed.Reason.EMAIL_NOT_VERIFIED, "Email address is not verified");
    }

    // Ignore the case of nickname
    if (!dto.getNickname().equalsIgnoreCase(packet.getNickname())) {
      return new ResetPasswordFailed(ResetPasswordFailed.Reason.WRONG_NICKNAME, "Wrong nickname");
    }

    String resetToken = tokenService.generate();
    playerStorage.updatePasswordResetToken(dto, resetToken);
    try {
      messagingService.send(packet.getEmail(), "Password reset", "To reset your password, use the following token: " + resetToken);
    } catch (MessageDeliveryFailedException e) {
      logger.debug("Token delivery failed, token is {}", resetToken);
      return new ResetPasswordFailed(ResetPasswordFailed.Reason.TOKEN_DELIVERY_FAILED, "Token delivery failed");
    }

    return new ResetPasswordSuccessful();
  }

  /**
   * Handles email verification.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleVerifyEmailPacket(Player player, VerifyEmail packet) {
    logger.debug("Handling verify email packet from player {}", player);

    if (!Settings.EmailVerification.ENABLED) {
      return new VerifyEmailFailed(VerifyEmailFailed.Reason.DISABLED,
          "Email verification is disabled");
    }

    PlayerDto playerDto = playerStorage.findByEmail(packet.getEmail());
    // TODO: This can be abused to find valid email addresses!
    if (playerDto == null) {
      return new VerifyEmailFailed(VerifyEmailFailed.Reason.EMAIL_NOT_FOUND, "Email not found");
    }

    if (playerDto.isEmailVerified()) {
      return new VerifyEmailFailed(VerifyEmailFailed.Reason.ALREADY_VERIFIED,
          "Email has already been verified");
    }

    if (playerDto.getEmailVerifyToken() == null || !playerDto.getEmailVerifyToken().equals(
        packet.getToken())) {
      return new VerifyEmailFailed(VerifyEmailFailed.Reason.INVALID_TOKEN, "Invalid token");
    }

    playerStorage.verifyEmail(playerDto);

    return new VerifyEmailSuccess();
  }

  /**
   * Handles login.
   *
   * @param player
   * @param packet
   * @return
   */
  private Packet handleLoginPacket(Player player, Login packet) {
    logger.debug("Handling login packet from player {}", player);

    if (player.isAuthenticated()) {
      logger.info("Player {} is already authenticated", player);
      return new LoginFailed(LoginFailed.Reason.ALREADY_LOGGED_IN, "You are already logged in");
    }

    PlayerDto dto = playerStorage.findByEmail(packet.getEmail());

    // TODO: Timing attacks possible!
    if (dto == null) {
      logger.info("Login from player {} failed, wrong username", player);
      return new LoginFailed(LoginFailed.Reason.CREDENTIALS_INVALID,
          "Invalid username or password");
    }

    if (!passwordHashService.verify(packet.getPassword(), dto.getPassword())) {
      logger.info("Login from player {} failed, wrong password", player);
      return new LoginFailed(LoginFailed.Reason.CREDENTIALS_INVALID,
          "Invalid username or password");
    }

    if (Settings.EmailVerification.ENABLED && !dto.isEmailVerified()) {
      logger.debug("Login from player {} failed, email is not verified", player);
      return new LoginFailed(LoginFailed.Reason.EMAIL_NOT_VERIFIED, "Email hasn't been verified");
    }

    if (players.findPlayerByNickname(dto.getNickname()) != null) {
      return new LoginFailed(LoginFailed.Reason.MULTIPLE_LOGIN, "Multiple logins are not allowed");
    }

    logger.info("Player {} logged in as {}", player, dto.getNickname());
    player.setAuthenticated(true);
    player.setNickname(dto.getNickname());
    player.setEmail(dto.getEmail());

    communicationService.multicast(players.findInLobby().except(player),
        new PlayerJoined(player));

    return new LoginSuccessful(dto.getNickname(), TimeService.getInstance().getTicks());
  }

}
