package cl.bgmp.utilsbukkit.timeutils;

public enum TimeConstant {
  MILLIS_IN_SECOND(1000.0),
  SECONDS_IN_MINUTE(60.0),
  MINUTES_IN_HOUR(60.0),
  HOURS_IN_DAY(24.0),
  DAYS_IN_WEEK(7.0),
  WEEKS_IN_MONTH(4.0),
  MONTHS_IN_YEAR(12.0),

  /**
   * Original precise value of the amount of weeks there is in a month Source: {@link
   * {https://duckduckgo.com/?q=How+many+weeks+are+in+a+month%3F&t=ffab&ia=answer}} Calculated
   * dividing 1 {@link TimeUnit#MONTHS}'s millis by 1 {@link TimeUnit#WEEKS}'s millis
   *
   * <p>Requires the library to be re-thought in order to be applied correctly.
   */
  WEEKS_IN_MONTH_PRECISE(4.3482142857142857142857142857143);

  private double value;

  TimeConstant(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }
}
