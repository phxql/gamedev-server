package edu.hm.gamedev.server.services.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A dummy messaging service.
 */
public class DummyMessagingService implements MessagingService {

  private static Logger logger = LoggerFactory.getLogger(DummyMessagingService.class);

  @Override
  public void send(String recipient, String subject, String message) throws MessageDeliveryFailedException {
    logger.debug("Message '{}' with subject '{}' to {}", message, subject, recipient);
  }
}
