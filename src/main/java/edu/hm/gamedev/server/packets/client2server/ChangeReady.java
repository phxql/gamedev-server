package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class ChangeReady extends Packet {

  private final boolean ready;

  @JsonCreator
  public ChangeReady(@JsonProperty("ready") boolean ready) {
    super(Type.CHANGE_READY);

    this.ready = ready;
  }

  @Override
  public String toString() {
    return "ChangeReady{" +
           "ready=" + ready +
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

    ChangeReady that = (ChangeReady) o;

    if (ready != that.ready) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (ready ? 1 : 0);
    return result;
  }

  public boolean isReady() {

    return ready;
  }
}
