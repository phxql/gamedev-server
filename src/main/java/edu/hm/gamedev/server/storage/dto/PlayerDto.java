package edu.hm.gamedev.server.storage.dto;

public class PlayerDto {

  private final String nickname;

  private final String email;

  private final String password;

  private final boolean emailVerified;

  private final String emailVerifyToken;

  private final String resetPasswordToken;

  public PlayerDto(String nickname, String email, String password, boolean emailVerified,
                   String emailVerifyToken, String resetPasswordToken) {
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.emailVerified = emailVerified;
    this.emailVerifyToken = emailVerifyToken;
    this.resetPasswordToken = resetPasswordToken;
  }

  public String getResetPasswordToken() {
    return resetPasswordToken;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public String getEmailVerifyToken() {
    return emailVerifyToken;
  }

  public String getPassword() {
    return password;
  }
}
