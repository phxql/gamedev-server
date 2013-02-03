package edu.hm.gamedev.server.services.messaging;

/**
 * Is thrown if a message delivery failed.
 */
public class MessageDeliveryFailedException extends Exception {
  public MessageDeliveryFailedException() {
  }

  public MessageDeliveryFailedException(String message) {
    super(message);
  }

  public MessageDeliveryFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageDeliveryFailedException(Throwable cause) {
    super(cause);
  }
}
