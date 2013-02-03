package edu.hm.gamedev.server.storage.hibernate.exception;

public class HibernateException extends RuntimeException {

  public HibernateException() {
  }

  public HibernateException(String message) {
    super(message);
  }

  public HibernateException(String message, Throwable cause) {
    super(message, cause);
  }

  public HibernateException(Throwable cause) {
    super(cause);
  }
}
