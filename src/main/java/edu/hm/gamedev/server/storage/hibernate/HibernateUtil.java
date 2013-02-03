package edu.hm.gamedev.server.storage.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton to use hibernate.
 */
public final class HibernateUtil {
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
  /**
   * Session factory.
   */
  private static SessionFactory sessionFactory;

  /**
   * Singleton, no instances allowed.
   */
  private HibernateUtil() {
  }

  /**
   * Gets the session factory.
   *
   * @return Session factory.
   */
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * Initializes the hibernate system.
   */
  public static void init() {
    logger.debug("Initializing hibernate...");

    Configuration configuration = new Configuration().configure();

    logger.debug("Performing schema update");
    new SchemaUpdate(configuration).execute(true, true);

    ServiceRegistry serviceRegistry =
        new ServiceRegistryBuilder().applySettings(configuration.getProperties())
            .buildServiceRegistry();
    sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    logger.debug("Hibernate initialized");
  }

  /**
   * Shuts down the hibernate system.
   */
  public static void shutdown() {
    logger.debug("Shutting down hibernate...");

    sessionFactory.close();

    logger.debug("Hibernate shutted down");
  }
}
