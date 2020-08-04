package com.wataandroidteam.calendarcustom.models;

import android.graphics.Color;
import android.text.TextUtils;
import java.util.Date;

public class MonthEvent {

  private String id;
  private String title;
  private Date startDate;
  private int bgColor = Color.WHITE;
  private int[] eventBottomColor;
  private int startDayIndex;
  private int dayCount;
  private int originalStartDayIndex;
  private boolean isAllDay;
  private int numberEventOnDay;
  private int numberLineOfTitle = 1;
  private boolean isSameDayWithOtherEvent;

  public MonthEvent(String id, String title, Date startDate, int startDayIndex,
      int dayCount, int originalStartDayIndex, boolean isAllDay) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
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

  public int getBackgroundColor() {
    return bgColor;
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

  public int getNumberEventOnDay() {
    return numberEventOnDay;
  }

  public void setNumberEventOnDay(int numberEventOnDay) {
    this.numberEventOnDay = numberEventOnDay;
  }

  public int[] getEventBottomColor() {
    return eventBottomColor;
  }

  public void setEventBottomColor(int[] eventBottomColor) {
    this.eventBottomColor = eventBottomColor;
  }

  public int getNumberLineOfTitle() {
    if (!TextUtils.isEmpty(getTitle()) && getTitle().contains("\n")) {
      String[] arrays = getTitle().split("\n");
      numberLineOfTitle = arrays.length;
    }
    return numberLineOfTitle;
  }

  public void setSameDayWithOtherEvent(boolean sameDayWithOtherEvent) {
    isSameDayWithOtherEvent = sameDayWithOtherEvent;
  }

  public boolean isSameDayWithOtherEvent() {
    return isSameDayWithOtherEvent;
  }
}
