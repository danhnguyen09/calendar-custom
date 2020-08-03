package com.android.calendars.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class DayMonthly {

  private int value;
  private boolean isThisMonth;
  private boolean isToday;
  private Date code;
  private int weekOfYear;
  private List<Event> dayEvents;
  private int indexOnMonthView;

  public DayMonthly(int value, boolean isThisMonth, boolean isToday, Date code, int weekOfYear,
      List<Event> dayEvents, int indexOnMonthView) {
    this.value = value;
    this.isThisMonth = isThisMonth;
    this.isToday = isToday;
    this.code = code;
    this.weekOfYear = weekOfYear;
    this.dayEvents = dayEvents;
    this.indexOnMonthView = indexOnMonthView;
  }

  public boolean isToday() {
    return isToday;
  }

  public boolean isThisMonth() {
    return isThisMonth;
  }

  public int getIndexOnMonthView() {
    return indexOnMonthView;
  }

  public int getValue() {
    return value;
  }

  public Date getCode() {
    return code;
  }

  public List<Event> getDayEvents() {
    return dayEvents;
  }

  public void setDayEvents(List<Event> dayEvents) {
    this.dayEvents = dayEvents;
  }
}
