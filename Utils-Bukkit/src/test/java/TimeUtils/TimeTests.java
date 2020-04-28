package TimeUtils;

import static org.junit.Assert.assertEquals;

import cl.bgmp.utilsbukkit.TimeUtils.Time;
import cl.bgmp.utilsbukkit.TimeUtils.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TimeTests {
  @Test
  public void testMillisEffectiveness() {
    Time millis = new Time(999, TimeUnit.MILLISECONDS);
    String effectiveMillis = "999 milliseconds";
    assertEquals(effectiveMillis, millis.toEffectiveString());
  }

  @Test
  public void testSecondsEffectiveness() {
    Time millis = new Time(2000, TimeUnit.MILLISECONDS);
    String effectiveSeconds = "2 seconds";
    assertEquals(effectiveSeconds, millis.toEffectiveString());
  }

  @Test
  public void testMinutesEffectiveness() {
    Time seconds = new Time(120, TimeUnit.SECONDS);
    String effectiveMinutes = "2 minutes";
    assertEquals(effectiveMinutes, seconds.toEffectiveString());
  }

  @Test
  public void testHoursEffectiveness() {
    Time minutes = new Time(120, TimeUnit.MINUTES);
    String effectiveHours = "2 hours";
    assertEquals(effectiveHours, minutes.toEffectiveString());
  }

  @Test
  public void testDaysEffectiveness() {
    Time hours = new Time(48, TimeUnit.HOURS);
    String effectiveDays = "2 days";
    assertEquals(effectiveDays, hours.toEffectiveString());
  }

  @Test
  public void testWeeksEffectiveness() {
    Time days = new Time(14, TimeUnit.DAYS);
    String effectiveWeeks = "2 weeks";
    assertEquals(effectiveWeeks, days.toEffectiveString());
  }

  @Test
  public void testMonthsEffectiveness() {
    Time weeks = new Time(8, TimeUnit.WEEKS);
    String effectiveMonths = "2 months";
    assertEquals(effectiveMonths, weeks.toEffectiveString());
  }

  @Test
  public void testYearsEffectiveness() {
    Time months = new Time(24, TimeUnit.MONTHS);
    String effectiveYears = "2 years";
    assertEquals(effectiveYears, months.toEffectiveString());
  }
}
