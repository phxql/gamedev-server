package edu.hm.gamedev.server.storage.hibernate.exception;

public class PlayerNotFoundException extends HibernateException {

  public PlayerNotFoundException() {
    super("Player not found");
  }
}
