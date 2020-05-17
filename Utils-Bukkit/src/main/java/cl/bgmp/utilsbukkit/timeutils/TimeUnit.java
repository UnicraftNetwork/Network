package cl.bgmp.utilsbukkit.timeutils;

import cl.bgmp.utilsbukkit.translations.Translations;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public enum TimeUnit {
  MILLISECONDS,
  SECONDS,
  MINUTES,
  HOURS,
  DAYS,
  WEEKS,
  MONTHS,
  YEARS;

  /**
   * @param string The unit in the form of a string, i.e: "h" == {@link TimeUnit#HOURS}
   * @return The parsed {@link TimeUnit} type
   */
  @NotNull
  public static TimeUnit fromString(final String string) {
    if (string.equalsIgnoreCase("l") | string.equalsIgnoreCase("millis")) {
      return TimeUnit.MILLISECONDS;
    } else if (string.equalsIgnoreCase("s") | string.equalsIgnoreCase("seconds")) {
      return TimeUnit.SECONDS;
    } else if (string.equalsIgnoreCase("m") | string.equalsIgnoreCase("minutes")) {
      return TimeUnit.MINUTES;
    } else if (string.equalsIgnoreCase("h") | string.equalsIgnoreCase("hours")) {
      return TimeUnit.HOURS;
    } else if (string.equalsIgnoreCase("d") | string.equalsIgnoreCase("days")) {
      return TimeUnit.DAYS;
    } else if (string.equalsIgnoreCase("w") | string.equalsIgnoreCase("weeks")) {
      return TimeUnit.WEEKS;
    } else if (string.equalsIgnoreCase("o") | string.equalsIgnoreCase("months")) {
      return TimeUnit.MONTHS;
    } else if (string.equalsIgnoreCase("y") | string.equalsIgnoreCase("years")) {
      return TimeUnit.YEARS;
    } else {
      throw new IllegalArgumentException(string + " is not a valid unit type");
    }
  }

  @NotNull
  public static TimeUnit effectiveUnit(@NotNull Time time) {
    return time.getUnit();
  }

  /**
   * @param charAt Character of the desired {@link TimeUnit} type
   * @return The deduced {@link TimeUnit}
   */
  public static TimeUnit fromChar(final char charAt) {
    return TimeUnit.fromString(String.valueOf(charAt));
  }

  /**
   * Reverts milliseconds to their {@link Time} form
   *
   * @param millisAmount Amount of milliseconds
   * @return The reconstructed {@link Time} form, calculated from the provided milliseconds amount
   */
  public static Time revertTimeFromMillis(long millisAmount) {
    if (millisAmount < TimeConstant.MILLIS_IN_SECOND.getValue())
      return new Time(millisAmount, MILLISECONDS);

    final long secondsAmount = (long) (millisAmount / TimeConstant.MILLIS_IN_SECOND.getValue());
    if (secondsAmount < TimeConstant.SECONDS_IN_MINUTE.getValue())
      return new Time(secondsAmount, SECONDS);

    final long minutesAmount = (long) (secondsAmount / TimeConstant.SECONDS_IN_MINUTE.getValue());
    if (minutesAmount < TimeConstant.MINUTES_IN_HOUR.getValue())
      return new Time(minutesAmount, MINUTES);

    final long hoursAmount = (long) (minutesAmount / TimeConstant.MINUTES_IN_HOUR.getValue());
    if (hoursAmount < TimeConstant.HOURS_IN_DAY.getValue()) return new Time(hoursAmount, HOURS);

    final long daysAmount = (long) (hoursAmount / TimeConstant.HOURS_IN_DAY.getValue());
    if (daysAmount < TimeConstant.DAYS_IN_WEEK.getValue()) return new Time(daysAmount, DAYS);

    final long weeksAmount = (long) (daysAmount / TimeConstant.DAYS_IN_WEEK.getValue());
    if (weeksAmount < TimeConstant.WEEKS_IN_MONTH.getValue()) return new Time(weeksAmount, WEEKS);

    final double monthsAmount = (weeksAmount / TimeConstant.WEEKS_IN_MONTH.getValue());
    if (monthsAmount < TimeConstant.MONTHS_IN_YEAR.getValue())
      return new Time((long) monthsAmount, MONTHS);

    final long yearsAmount = (long) (monthsAmount / TimeConstant.MONTHS_IN_YEAR.getValue());
    if (yearsAmount > 1) return new Time(yearsAmount, YEARS);

    throw new IllegalArgumentException("Invalid amount of milliseconds");
  }

  /**
   * Reverts milliseconds to their {@link TimeUnit} form
   *
   * @param millisAmount Amount of milliseconds
   * @return The isolated {@link TimeUnit} form, calculated from the provided milliseconds amount
   */
  public static TimeUnit revertUnitFromMillis(long millisAmount) {
    if (millisAmount < TimeConstant.MILLIS_IN_SECOND.getValue()) return MILLISECONDS;

    final long secondsAmount = (long) (millisAmount / TimeConstant.MILLIS_IN_SECOND.getValue());
    if (secondsAmount < TimeConstant.SECONDS_IN_MINUTE.getValue()) return SECONDS;

    final long minutesAmount = (long) (secondsAmount / TimeConstant.SECONDS_IN_MINUTE.getValue());
    if (minutesAmount < TimeConstant.MINUTES_IN_HOUR.getValue()) return MINUTES;

    final long hoursAmount = (long) (minutesAmount / TimeConstant.MINUTES_IN_HOUR.getValue());
    if (hoursAmount < TimeConstant.HOURS_IN_DAY.getValue()) return HOURS;

    final long daysAmount = (long) (hoursAmount / TimeConstant.HOURS_IN_DAY.getValue());
    if (daysAmount < TimeConstant.DAYS_IN_WEEK.getValue()) return DAYS;

    final long weeksAmount = (long) (daysAmount / TimeConstant.DAYS_IN_WEEK.getValue());
    if (weeksAmount < TimeConstant.WEEKS_IN_MONTH.getValue()) return WEEKS;

    final long monthsAmount = (long) (weeksAmount / TimeConstant.WEEKS_IN_MONTH.getValue());
    if (monthsAmount < TimeConstant.MONTHS_IN_YEAR.getValue()) return MONTHS;

    final long yearsAmount = (long) (monthsAmount / TimeConstant.MONTHS_IN_YEAR.getValue());
    if (yearsAmount > 1) return YEARS;

    throw new IllegalArgumentException("Invalid amount of milliseconds");
  }

  /** @return {@link TimeUnit}s milliseconds */
  public long millis() {
    switch (this) {
      case MILLISECONDS:
        return 1;
      case SECONDS:
        return 1000;
      case MINUTES:
        return 60000;
      case HOURS:
        return 3600000;
      case DAYS:
        return 86400000;
      case WEEKS:
        return 604800000;
      case MONTHS:
        return 2629800000L;
      case YEARS:
        return 31557600000L;
      default:
        throw new IllegalArgumentException(this + " is not a unit");
    }
  }

  @Override
  public String toString() {
    switch (this) {
      case DAYS:
        return "DAYS";
      case HOURS:
        return "HOURS";
      case WEEKS:
        return "WEEKS";
      case YEARS:
        return "YEARS";
      case MONTHS:
        return "MONTHS";
      case MINUTES:
        return "MINUTES";
      case SECONDS:
        return "SECONDS";
      case MILLISECONDS:
        return "MILLISECONDS";
      default:
        return "null";
    }
  }

  public String toLocalizedString(final CommandSender sender) {
    switch (this) {
      case DAYS:
        return Translations.get("time.unit.days", sender);
      case HOURS:
        return Translations.get("time.unit.hours", sender);
      case WEEKS:
        return Translations.get("time.unit.weeks", sender);
      case YEARS:
        return Translations.get("time.unit.years", sender);
      case MONTHS:
        return Translations.get("time.unit.months", sender);
      case MINUTES:
        return Translations.get("time.unit.minutes", sender);
      case SECONDS:
        return Translations.get("time.unit.seconds", sender);
      case MILLISECONDS:
        return Translations.get("time.unit.milliseconds", sender);
      default:
        return "null";
    }
  }
}
