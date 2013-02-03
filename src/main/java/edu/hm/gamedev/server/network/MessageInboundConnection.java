package edu.hm.gamedev.server.network;

import org.apache.catalina.websocket.MessageInbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.CharBuffer;

public class MessageInboundConnection implements Connection {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageInboundConnection.class);

  private final MessageInbound messageInbound;

  private boolean isConnected;

  public MessageInboundConnection(MessageInbound messageInbound) {
    this.messageInbound = messageInbound;
    this.isConnected = true;
  }

  @Override
  public void close() {
    if (this.isConnected()) {
      try {
        this.isConnected = false;
        this.messageInbound.getWsOutbound().close(0, null);
      } catch (IOException e) {
        LOGGER.warn("IO error when closing", e);
      }
    }
  }

  @Override
  public void send(final String text) {
    if (this.isConnected) {
      try {
        messageInbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(text));
      } catch (IOException e) {
        LOGGER.warn("IO error when sending text", e);
        close();
      }
    } else {
      LOGGER.warn("Tried to send text to a non-connected connection");
    }
  }

  @Override
  public boolean isConnected() {
    return this.isConnected;
  }
}
