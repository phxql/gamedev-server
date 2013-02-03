package edu.hm.gamedev.server.services.time;

public abstract class TimeService {
  private static TimeService instance = new CurrentTimeService();

  public static TimeService getInstance() {
    return TimeService.instance;
  }

  public static void setInstance(TimeService instance) {
    TimeService.instance = instance;
  }

  protected TimeService() {
  }

  public abstract long getTicks();
}
