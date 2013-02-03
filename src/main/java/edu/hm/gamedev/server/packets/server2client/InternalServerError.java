package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Packet to notify the client that an internal server error has occured.
 */
public class InternalServerError extends Packet {

  /**
   * Error details. Can be null.
   */
  private final String details;

  public InternalServerError(String details) {
    super(Type.INTERNAL_SERVER_ERROR);
    this.details = details;
  }

  @Override
  public String toString() {
    return "InternalServerError{" +
           "details='" + details + '\'' +
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

    InternalServerError that = (InternalServerError) o;

    if (details != null ? !details.equals(that.details) : that.details != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (details != null ? details.hashCode() : 0);
    return result;
  }

  public String getDetails() {

    return details;
  }
}
