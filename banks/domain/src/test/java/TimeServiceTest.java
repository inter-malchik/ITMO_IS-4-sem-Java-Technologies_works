import org.junit.jupiter.api.Test;
import time.TimeService;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeServiceTest {
  @Test
  void CheckTimeSkippable() {
    TimeService timeService = new TimeService();
    LocalDateTime previousTime = timeService.getDateTime();
    timeService.registerTimeDifference(Duration.ofDays(2));
    LocalDateTime laterTime = timeService.getDateTime();
    assertTrue(previousTime.isBefore(laterTime));
  }
}
