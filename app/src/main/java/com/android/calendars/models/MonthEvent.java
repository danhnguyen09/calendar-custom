package com.android.calendars.models;

import java.util.Date;

/**
 * Created by Danh Nguyen on 8/4/20.
 */
public class MonthEvent {
  private String id;
  private String title;
  private Date startDate;
  private int color;
  private int startDayIndex;
  private int dayCount;
  private int originalStartDayIndex;
  private boolean isAllDay;

  public MonthEvent(String id, String title, Date startDate, int color, int startDayIndex,
      int dayCount, int originalStartDayIndex, boolean isAllDay) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.color = color;
    this.startDayIndex = startDayIndex;
    this.dayCount = dayCount;
    this.originalStartDayIndex = originalStartDayIndex;
    this.isAllDay = isAllDay;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Date getStartDate() {
    return startDate;
  }

  public int getColor() {
    return color;
  }

  public int getStartDayIndex() {
    return startDayIndex;
  }

  public int getDayCount() {
    return dayCount;
  }

  public int getOriginalStartDayIndex() {
    return originalStartDayIndex;
  }

  public boolean isAllDay() {
    return isAllDay;
  }
}
