package com.android.calendars.ui.main;

import android.content.Context;
import com.android.calendars.DateHelper;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.models.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MonthlyCalendarImpl {

  private MonthlyCalendar monthlyCalendar;
  private Context context;
  private int DAYS_CNT = 42;

  public MonthlyCalendarImpl(MonthlyCalendar monthlyCalendar, Context context) {
    this.monthlyCalendar = monthlyCalendar;
    this.context = context;
  }

  public void getDays(long targetDate) {
    Calendar currentCal = Calendar.getInstance();
    currentCal.setTimeInMillis(targetDate);
    List<DayMonthly> days = new ArrayList<>(DAYS_CNT);
    int currMonthDays = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    currentCal.set(Calendar.DAY_OF_MONTH, 1);
    int firstDayIndex = currentCal.get(Calendar.DAY_OF_WEEK);
    currentCal.add(Calendar.MONTH, -1);
    int prevMonthDays = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);

    boolean isThisMonth = false;
    boolean isToday;
    int value = prevMonthDays - firstDayIndex + 2;
    currentCal.setTimeInMillis(targetDate);
    Date curDay = currentCal.getTime();
    for (int i = 1; i <= DAYS_CNT; i++) {
      if (firstDayIndex > i) {
        currentCal.setTimeInMillis(targetDate);
        isThisMonth = false;
        currentCal.set(Calendar.DAY_OF_MONTH, 1);
        currentCal.add(Calendar.MONTH, -1);
        curDay = currentCal.getTime();
      } else if (firstDayIndex == i) {
        value = 1;
        isThisMonth = true;
        currentCal.setTimeInMillis(targetDate);
        curDay = currentCal.getTime();
      } else if (value == currMonthDays + 1) {
        currentCal.setTimeInMillis(targetDate);
        value = 1;
        isThisMonth = false;
        currentCal.set(Calendar.DAY_OF_MONTH, 1);
        currentCal.add(Calendar.MONTH, 1);
        curDay = currentCal.getTime();
      }
      Calendar newCalendar = Calendar.getInstance();
      newCalendar.setTime(curDay);
      newCalendar.set(Calendar.DAY_OF_MONTH, value);
      Date newDay = newCalendar.getTime();
      isToday = DateHelper.isToday(newCalendar);
      DayMonthly day = new DayMonthly(value, isThisMonth, isToday, newDay,
          newCalendar.get(Calendar.WEEK_OF_YEAR), new ArrayList<Event>(), i - 1);
      days.add(day);
      value++;
    }
    currentCal.setTimeInMillis(targetDate);
    monthlyCalendar
        .updateMonthlyCalendar(context, DateHelper.getMonthName(context, targetDate), days);
  }

  public void makeEvent(List<Event> eventList, List<DayMonthly> days) {
    if (eventList == null || eventList.isEmpty()) {
      return;
    }
    HashMap<String, List<Event>> dayEvents = new HashMap<>();
    eventList.forEach(event -> {
      Date startEvent = event.getStartDate();
      Date endEvent = event.getEndDate();
      String startEventCode = DateHelper.getStringFromDate(startEvent);
      String endEventCode = DateHelper.getStringFromDate(endEvent);
      List<Event> events = dayEvents.get(startEventCode);
      if (events == null) {
        events = new ArrayList<>();
      }
      events.add(event);
      dayEvents.put(startEventCode, events);
      Date currentDate = startEvent;
      while (!DateHelper.getStringFromDate(currentDate).equalsIgnoreCase(endEventCode)) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        currentDate = calendar.getTime();
        startEventCode = DateHelper.getStringFromDate(currentDate);
        List<Event> eventListMoreDay = dayEvents.get(startEventCode);
        if (eventListMoreDay == null) {
          eventListMoreDay = new ArrayList<>();
        }
        eventListMoreDay.add(event);
        dayEvents.put(startEventCode, eventListMoreDay);
      }
    });
    days.forEach(dayMonthly -> {
      String key = DateHelper.getStringFromDate(dayMonthly.getCode());
      if (dayEvents.containsKey(key)) {
        dayMonthly.setDayEvents(dayEvents.get(key));
      }
    });
  }
}
