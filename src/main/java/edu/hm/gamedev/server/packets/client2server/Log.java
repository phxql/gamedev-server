//______________________________________________________________________________
//
//          Project:
//             File: $Id: Log.java 240 2012-11-18 18:24:16Z ifw10191 ${FILE_NAME} $
//     last changed: $Rev: 240 $
//______________________________________________________________________________
//
//       created by: christian
//    creation date: 20.10.12
//       changed by: $Author: ifw10191 $
//      change date: $LastChangedDate: 2012-11-18 19:24:16 +0100 (So, 18. Nov 2012) $
//      description:
//______________________________________________________________________________
//
//        Copyright: (c) Christian Fritz, all rights reserved
//______________________________________________________________________________
package edu.hm.gamedev.server.packets.client2server;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Type;

public class Log extends Packet {

  private final int level;
  private final String message;

  @JsonCreator
  public Log(@JsonProperty("level") int level, @JsonProperty("message") String message) {
    super(Type.LOG);
    this.level = level;
    this.message = message;
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

    Log log = (Log) o;

    if (level != log.level) {
      return false;
    }
    if (message != null ? !message.equals(log.message) : log.message != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + level;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }

  public int getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Log{" +
           "level=" + level +
           ", message='" + message + '\'' +
           '}';
  }
}
