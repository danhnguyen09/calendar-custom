package com.wataandroidteam.calendarcustom;

import com.wataandroidteam.calendarcustom.models.DayMonthly;
import com.wataandroidteam.calendarcustom.models.Event;
import java.util.List;

public interface IMonthListener {

  void onAddEvents(List<Event> events);

  List<Event> getEventList();

  void onDayMonthlyClicked(DayMonthly day);
}
