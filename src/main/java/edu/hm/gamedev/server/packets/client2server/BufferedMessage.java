package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class BufferedMessage extends Packet {

  private final long timestamp;

  private final JsonNode payload;

  @JsonCreator
  public BufferedMessage(@JsonProperty("timestamp") long timestamp,
                         @JsonProperty("payload") JsonNode payload) {
    super(Type.BUFFERED_MESSAGE);
    this.timestamp = timestamp;
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "BufferedMessage{" +
           "timestamp=" + timestamp +
           ", payload=" + payload +
           '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    BufferedMessage that = (BufferedMessage) o;

    if (timestamp != that.timestamp) {
      return false;
    }
    if (payload != null ? !payload.equals(that.payload) : that.payload != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
    result = 31 * result + (payload != null ? payload.hashCode() : 0);
    return result;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public JsonNode getPayload() {
    return payload;
  }
}
