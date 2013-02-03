package edu.hm.gamedev.server.packets.server2client;

import org.codehaus.jackson.JsonNode;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class MapStorage extends Packet {

  /**
   * Content.
   */
  private final JsonNode content;

  public MapStorage(JsonNode content) {
    super(Type.MAP_STORAGE);
    this.content = content;
  }

  @Override
  public String toString() {
    return "MapStorage{" +
           "content=" + content +
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

    MapStorage that = (MapStorage) o;

    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }

  public JsonNode getContent() {

    return content;
  }
}
