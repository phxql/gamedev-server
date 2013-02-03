package edu.hm.gamedev.server.packets.server2client;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

/**
 * Hello packet.
 */
public class Hello extends Packet {

  private final String buildNumber;
  private final String buildDate;
  private final String buildRev;

  public Hello(String number, String date, String rev) {
    super(Type.HELLO);
    this.buildDate = date;
    this.buildRev = rev;
    this.buildNumber = number;
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

    Hello hello = (Hello) o;

    if (buildDate != null ? !buildDate.equals(hello.buildDate) : hello.buildDate != null) {
      return false;
    }
    if (buildNumber != null ? !buildNumber.equals(hello.buildNumber) : hello.buildNumber != null) {
      return false;
    }
    if (buildRev != null ? !buildRev.equals(hello.buildRev) : hello.buildRev != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (buildNumber != null ? buildNumber.hashCode() : 0);
    result = 31 * result + (buildDate != null ? buildDate.hashCode() : 0);
    result = 31 * result + (buildRev != null ? buildRev.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Hello{" +
           "buildDate='" + buildDate + '\'' +
           ", buildNumber='" + buildNumber + '\'' +
           ", buildRev='" + buildRev + '\'' +
           '}';
  }

  public String getBuildDate() {
    return buildDate;
  }

  public String getBuildNumber() {
    return buildNumber;
  }

  public String getBuildRev() {
    return buildRev;
  }
}
