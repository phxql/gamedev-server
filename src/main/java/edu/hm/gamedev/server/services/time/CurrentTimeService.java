package edu.hm.gamedev.server.services.time;

public class CurrentTimeService extends TimeService {
  @Override
  public long getTicks() {
    return System.currentTimeMillis();
  }
}
