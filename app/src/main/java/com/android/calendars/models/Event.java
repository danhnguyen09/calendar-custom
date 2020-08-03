package com.android.calendars.models;

import java.util.Date;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class Event {
  private String id;
  private String title;
  private Date startDate;
  private Date endDate;
  private String description;

  public Event(String id, String title, Date startDate, Date endDate, String description) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
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

  public String getDescription() {
    return description;
  }
}
