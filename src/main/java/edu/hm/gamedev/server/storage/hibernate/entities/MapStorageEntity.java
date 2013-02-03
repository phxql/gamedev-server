package edu.hm.gamedev.server.storage.hibernate.entities;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"game", "map"}))
public class MapStorageEntity {

  @Id
  @GeneratedValue
  private int id;

  @Column
  private String game;

  @Column
  private String map;

  @Column
  @Lob
  private String content;

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

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }
}
