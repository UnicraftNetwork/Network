package cl.bgmp.utilsbukkit.timeutils;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class Time {
  private final long value;
  private @NotNull final TimeUnit unit;

  /**
   * @param value The number of time units
   * @param unit The specific {@link TimeUnit} this {@link Time} instance is handled in
   */
  public Time(final long value, @NotNull final TimeUnit unit) {
    this.value = value;
    this.unit = unit;
  }

  /** @return The time's value. The amount of {@link TimeUnit}s */
  public long getValue() {
    return value;
  }

  /** @return The {@link TimeUnit} type for this specific time object */
  @NotNull
  public TimeUnit getUnit() {
    return unit;
  }

  /**
   * @param string Time string
   * @return Formatted string: value {@link TimeUnit}
   */
  public static Time fromString(@NotNull final String string) {
    final long value = Long.parseLong(string.substring(0, string.length() - 1));
    final TimeUnit unit = TimeUnit.fromChar(string.charAt(string.length() - 1));
    return new Time(value, unit);
  }

  @Deprecated
  public Time getAs(@NotNull final TimeUnit unit) {
    return new Time(this.millis() / unit.millis(), unit);
  }

  /**
   * Created for Bukkit development
   *
   * @return Minecraft ticks
   */
  public long ticks() {
    return (millis() * 20) / 1000;
  }

  /**
   * Considering the number of {@link TimeUnit}s, calculates the corresponding milliseconds
   *
   * @return Time instance's milliseconds
   */
  private long millis() {
    return this.value * this.unit.millis();
  }

  /** @return The next Java {@link Date} available */
  public Date nextDate() {
    return new Date(nextDateMillis());
  }

  /** @return A sum of the current system millis and the {@link Time#millis()} */
  private long nextDateMillis() {
    return System.currentTimeMillis() + this.millis();
  }

  /**
   * Deduces what is the maximum time {@link TimeUnit} that can be created off {@link this#unit},
   * and formats it as a string
   *
   * @return Formatted deduced {@link Time} string
   */
  @NotNull
  public String toEffectiveString() {
    Time time = TimeUnit.revertTimeFromMillis(this.millis());
    return time.toString();
  }

  /**
   * Deduces what is the maximum time {@link TimeUnit} that can be created off {@link this#unit},
   * and formats it as a minimal string
   *
   * @return Formatted deduced {@link Time} minimal string
   */
  @NotNull
  public String toEffectiveMinimalString() {
    Time time = TimeUnit.revertTimeFromMillis(this.millis());
    return time.toMinimalString();
  }

  /** @return The literal amount of {@link TimeUnit}s, formatted as a string */
  @Override
  public String toString() {
    return this.value + " " + unit.toString().toLowerCase();
  }

  /**
   * @return The minimal string which can be parsed off the current {@link Time} instance. i.e: 3s,
   *     5d, 10o, etc.
   */
  public String toMinimalString() {
    if (unit.equals(TimeUnit.MONTHS))
      return this.value
          + "o"; // Months's first character doesn't match its first letter (m stands for minutes)
    else return this.value + String.valueOf(unit.toString().toCharArray()[0]).toLowerCase();
  }
}
