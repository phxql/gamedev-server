package edu.hm.gamedev.server.storage.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PlayerEntity {

  /**
   * Id.
   */
  @Id
  @GeneratedValue
  private int id;

  /**
   * Nickname.
   */
  @Column(unique = true)
  private String nickname;

  /**
   * Email.
   */
  @Column(unique = true)
  private String email;

  /**
   * Password.
   */
  @Column
  private String password;

  /**
   * True if the email has been verified.
   */
  @Column
  private boolean emailVerified;

  /**
   * Token to verify the email.
   */
  @Column(nullable = true)
  private String emailVerifyToken;

  /**
   * Token to set a new password.
   */
  @Column(nullable = true)
  private String resetPasswordToken;

  public String getResetPasswordToken() {
    return resetPasswordToken;
  }

  public void setResetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public String getEmailVerifyToken() {
    return emailVerifyToken;
  }

  public void setEmailVerifyToken(String emailVerifyToken) {
    this.emailVerifyToken = emailVerifyToken;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
