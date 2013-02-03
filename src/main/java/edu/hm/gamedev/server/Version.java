package edu.hm.gamedev.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Version {
  private static Properties properties;

  private static Logger logger = LoggerFactory.getLogger(Version.class);

  private Version() {
  }

  private static synchronized void loadProperties() {
    if (properties == null) {
      properties = new Properties();
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      InputStream is = cl.getResourceAsStream("build.properties");

      try {
        properties.load(is);
      } catch (IOException e) {
        logger.error("Can not load build information.", e);
      }
    }
  }

  public static String getBuildNumber() {
    loadProperties();
    return properties.getProperty("gamedev.build.number", "-1");
  }

  public static String getBuildRevision() {
    loadProperties();
    return properties.getProperty("gamedev.build.revision", "-1");
  }

  public static String getBuildDate() {
    loadProperties();
    return properties.getProperty("gamedev.build.date", "1970-01-01_00:00");
  }
}
