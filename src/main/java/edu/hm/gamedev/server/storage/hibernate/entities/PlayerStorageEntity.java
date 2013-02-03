package edu.hm.gamedev.server.storage.hibernate.entities;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"game", "player_id"}))
public class PlayerStorageEntity {

  @Id
  @GeneratedValue
  private int id;

  @Column
  private String game;

  @ManyToOne
  private PlayerEntity player;

  @Column
  @Lob
  private String content;

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public PlayerEntity getPlayer() {
    return player;
  }

  public void setPlayer(PlayerEntity player) {
    this.player = player;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
