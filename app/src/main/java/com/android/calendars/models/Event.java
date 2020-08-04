package com.android.calendars.models;

import java.util.Date;

public class Event {

  private String id;
  private String title;
  private Date startDate;
  private Date endDate;
  private int[] bottomColors;

  public Event(String id, String title, Date startDate, Date endDate) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
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

  public Date getEndDate() {
    return endDate;
  }

  public int[] getBottomColors() {
    return bottomColors;
  }

  public void setBottomColors(int[] bottomColors) {
    this.bottomColors = bottomColors;
  }
}
