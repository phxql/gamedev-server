package edu.hm.gamedev.server.services.messaging;

/**
 * Service to send a message.
 */
public interface MessagingService {

  /**
   * Sends a message.
   *
   * @param recipient Recipient.
   * @param message   Message.
   * @param subject Subject,
   * @throws MessageDeliveryFailedException If the message delivery failed.
   */
  void send(String recipient, String subject, String message) throws MessageDeliveryFailedException;
}
