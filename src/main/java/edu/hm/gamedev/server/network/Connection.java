package edu.hm.gamedev.server.network;

/**
 * A connection to a client.
 */
public interface Connection {
  /**
   * Closes the connection.
   */
  void close();

  /**
   * Sends text data to the client.
   * @param text Text data.
   */
  void send(String text);

  /**
   * Returns true if the connection is connected.
   *
   * @return True if the connection is connected.
   */
  boolean isConnected();
}
