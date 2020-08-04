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

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthlyCalendarImpl {

  private MonthlyCalendar monthlyCalendar;
  private Context context;
  private int DAYS_CNT = 42;
  private String YEAR_PATTERN = "YYYY";
  private List<Event> mEvents = new ArrayList<>();

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
    makeEvent(days);
    monthlyCalendar
        .updateMonthlyCalendar(context, DateHelper.getMonthName(context, targetDate), days, false,
            currentCal.getTime());
  }

  private Date createEventDate(int day, int month, int year) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.YEAR, year);
    return calendar.getTime();
  }

  private void makeEvent(List<DayMonthly> days) {
    Event event1 = new Event("001", "1. Event test 01", createEventDate(10, 8, 2020),
        createEventDate(10, 8, 2020));
    Event event2 = new Event("002", "2. Event test 02", createEventDate(10, 8, 2020),
        createEventDate(10, 8, 2020));
    Event event3 = new Event("003", "3. Event test 03", createEventDate(10, 8, 2020),
        createEventDate(10, 8, 2020));
    Event event4 = new Event("004", "4. Event test 04", createEventDate(10, 8, 2020),
        createEventDate(10, 8, 2020));
    Event event5 = new Event("005", "5. Event test 05\nService:", createEventDate(4, 8, 2020),
        createEventDate(4, 8, 2020));
    Event event6 = new Event("006", "6.Event test 06", createEventDate(8, 8, 2020),
        createEventDate(15, 8, 2020));
    Event event7 = new Event("007", "7. Event test 07", createEventDate(28, 8, 2020),
        createEventDate(3, 9, 2020));
    Event event8 = new Event("008", "8. Event test 08", createEventDate(6, 8, 2020),
        createEventDate(10, 9, 2020));

    mEvents.add(event1);
    mEvents.add(event2);
    mEvents.add(event3);
    mEvents.add(event4);
    mEvents.add(event5);
    mEvents.add(event6);
//    mEvents.add(event7);
    mEvents.add(event8);

    HashMap<String, List<Event>> dayEvents = new HashMap<>();
    mEvents.forEach(event -> {
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
        List<Event> eventList = dayEvents.get(startEventCode);
        if (eventList == null) {
          eventList = new ArrayList<>();
        }
        eventList.add(event);
        dayEvents.put(startEventCode, eventList);
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
