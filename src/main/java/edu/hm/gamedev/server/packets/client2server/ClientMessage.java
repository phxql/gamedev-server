package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class ClientMessage extends Packet {

  private final long timestamp;

  private final JsonNode payload;

  private final String contentType;

  @JsonCreator
  public ClientMessage(@JsonProperty("timestamp") long timestamp,
                       @JsonProperty("payload") JsonNode payload,
                       @JsonProperty("contentType") String contentType) {

    super(Type.CLIENT_MESSAGE);
    this.timestamp = timestamp;
    this.payload = payload;
    this.contentType = contentType;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public JsonNode getPayload() {
    return payload;
  }

  public String getContentType() {
    return contentType;
  }

  @Override
  public String toString() {
    return "ClientMessage{" +
           "timestamp=" + timestamp +
           ", payload=" + payload +
           ", contentType='" + contentType + '\'' +
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

    ClientMessage that = (ClientMessage) o;

    if (timestamp != that.timestamp) {
      return false;
    }
    if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) {
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
    result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
    return result;
  }
}
