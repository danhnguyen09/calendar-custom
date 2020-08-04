package com.android.calendars.ui.main;


import com.android.calendars.models.DayMonthly;
import com.android.calendars.models.Event;
import java.util.List;

public interface IMonthListener {

  void onAddEvents(List<Event> events);

  List<Event> getEventList();

  void onDayMonthlyClicked(DayMonthly day);
}
