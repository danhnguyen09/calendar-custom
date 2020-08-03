package com.android.calendars.ui.main;

import android.content.Context;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.models.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
      isToday = isToday(newCalendar);
      DayMonthly day = new DayMonthly(value, isThisMonth, isToday, newDay,
          newCalendar.get(Calendar.WEEK_OF_YEAR), new ArrayList<Event>(), i - 1);
      days.add(day);
      value++;
    }
    currentCal.setTimeInMillis(targetDate);
    monthlyCalendar.updateMonthlyCalendar(context, getMonthName(targetDate), days, false,
        currentCal.getTime());
  }

  private boolean isToday(Calendar calendar) {
    Calendar current = Calendar.getInstance();
    return calendar.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)
        && calendar.get(Calendar.MONTH) == current.get(Calendar.MONTH)
        && calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR);
  }

  private String getMonthName(long targetDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(targetDate);
    return getMonthName(context, calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
  }

  private String getMonthName(Context context, int id) {
    return context.getResources().getStringArray(R.array.months)[id];
  }
}
