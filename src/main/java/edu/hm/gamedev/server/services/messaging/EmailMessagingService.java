package edu.hm.gamedev.server.services.messaging;

import edu.hm.gamedev.server.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailMessagingService implements MessagingService {

  private static Logger logger = LoggerFactory.getLogger(EmailMessagingService.class);


  /**
   * Email settings.
   */
  private static class EmailSettings {
    /**
     * Properties to configure the session.
     */
    private Properties properties;

    /**
     * Authenticator for session.
     */
    private Authenticator authenticator;

    public EmailSettings() {
      properties = new Properties();

      properties.put("mail.smtp.host", Settings.EmailSettings.SMTP_HOST);
      properties.put("mail.smtp.port", Settings.EmailSettings.PORT);
      properties.put("mail.smtp.auth", true);
      properties.put("mail.smtp.starttls.enable", true);

      authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(Settings.EmailSettings.USER, Settings.EmailSettings.PASSWORD);
        }
      };
    }

    /**
     * Returns the authenticator for the session.
     */
    public Authenticator getAuthenticator() {
      return authenticator;
    }

    /**
     * Returns the properties to configure the session.
     *
     * @return Properties.
     */
    public Properties getProperties() {
      return properties;
    }
  }

  @Override
  public void send(String recipient, String subject, String message)
      throws MessageDeliveryFailedException {
    logger.debug("Sending email to {}", recipient);

    EmailSettings settings = new EmailSettings();

    try {
      Session
          session =
          Session.getDefaultInstance(settings.getProperties(), settings.getAuthenticator());

      MimeMessage email = new MimeMessage(session);
      email.setFrom(new InternetAddress(Settings.EmailSettings.FROM));
      email.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

      email.setSubject(subject);
      email.setText(message);

      Transport.send(email);

      logger.debug("Sent email '{}' to {}", message, recipient);
    } catch (MessagingException e) {
      logger.warn("Failed to send email '{}' to {}", message, recipient);
      logger.warn("Exception", e);

      throw new MessageDeliveryFailedException(e);
    }
  }
}
