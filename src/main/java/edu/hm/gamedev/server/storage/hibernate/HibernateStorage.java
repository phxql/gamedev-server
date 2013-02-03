package edu.hm.gamedev.server.storage.hibernate;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for every hibernate storage.
 */
abstract class HibernateStorage {

  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(HibernateStorage.class);

  /**
   * Interface to emulate a function with a Session argument.
   *
   * @param <T> Return Type of the apply method.
   */
  protected interface WithSessionR<T> {

    T apply(Session session);
  }

  /**
   * Interface to emulate a function with a session argument.
   */
  protected interface WithSession {

    void apply(Session session);
  }

  /**
   * Executes a piece of code in a hibernate session.
   *
   * @param withSession Piece of code wrapped in an interface.
   */
  protected void withSession(final WithSession withSession) {
    withSession(new WithSessionR<Object>() {
      @Override
      public Object apply(Session session) {
        withSession.apply(session);
        return null;
      }
    });
  }

  /**
   * Executes a piece of code in a hibernate session.
   *
   * @param withSession Piece of code wrapped in an interface.
   * @param <T>         Return Type of the method.
   * @return Returns what the given withSession interface returns from the apply method.
   */
  protected <T> T withSession(WithSessionR<T> withSession) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      session.beginTransaction();

      T result = withSession.apply(session);

      if (session.getTransaction().isActive()) {
        session.getTransaction().commit();
        logger.trace("Transaction commited");
      }

      return result;
    } catch (RuntimeException e) {
      logger.error("Exception while using session", e);

      if (session.getTransaction().isActive()) {
        session.getTransaction().rollback();
        logger.trace("Transaction rolled back");
      }

      throw e;
    } finally {
      session.close();
    }
  }
}
