package edu.hm.gamedev.server.storage.hibernate.entities;

import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Entity for a log entry.
 */
@Entity
public class LogEntryEntity {

  /**
   * Id.
   */
  @Id
  @GeneratedValue
  private int id;

  /**
   * Log message.
   */
  @Column
  @Lob
  private String message;

  /**
   * Log level.
   */
  @Column
  private int level;

  /**
   * Timestamp.
   */
  @Column
  private Timestamp timestamp;

  /**
   * Player which send the log entry.
   */
  @ManyToOne
  private PlayerEntity player;

  public Timestamp getTimestamp() {
    return (Timestamp)timestamp.clone();
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = (Timestamp)timestamp.clone();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getLevel() {
    return level;
  }

  public PlayerEntity getPlayer() {
    return player;
  }

  public void setPlayer(PlayerEntity player) {
    this.player = player;
  }

  public void setLevel(int level) {
    this.level = level;
  }


}
