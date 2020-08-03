package com.android.calendars.ui.main;

import android.content.Context;
import com.android.calendars.Constant;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.models.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthlyCalendarImpl {

  private MonthlyCalendar monthlyCalendar;
  private Context context;
  private int DAYS_CNT = 42;
  private String YEAR_PATTERN = "YYYY";
  private String mToday = new DateTime().toString(Constant.DAYCODE_PATTERN);
  private List<Event> mEvents = new ArrayList<>();

  private DateTime mTargetDate;

  public MonthlyCalendarImpl(MonthlyCalendar monthlyCalendar, Context context) {
    this.monthlyCalendar = monthlyCalendar;
    this.context = context;
  }

  public void setTargetDate(DateTime targetDate) {
    mTargetDate = targetDate;
  }

  public void getDays(boolean markDaysWithEvents) {
    List<DayMonthly> days = new ArrayList<DayMonthly>(DAYS_CNT);
    int currMonthDays = mTargetDate.dayOfMonth().getMaximumValue();
    int firstDayIndex = mTargetDate.withDayOfMonth(1).getDayOfWeek();
    if (Calendar.getInstance().getFirstDayOfWeek() != Calendar.SUNDAY) {
      firstDayIndex -= 1;
    }
    int prevMonthDays = mTargetDate.minusMonths(1).dayOfMonth().getMaximumValue();

    boolean isThisMonth = false;
    boolean isToday;
    int value = prevMonthDays - firstDayIndex + 1;
    DateTime curDay = mTargetDate;

    for (int i = 0; i < DAYS_CNT; i++) {
      if (firstDayIndex > i) {
        isThisMonth = false;
        curDay = mTargetDate.withDayOfMonth(1).minusMonths(1);
      } else if (firstDayIndex == i) {
        value = 1;
        isThisMonth = true;
        curDay = mTargetDate;
      } else if (value == currMonthDays + 1) {
        value = 1;
        isThisMonth = false;
        curDay = mTargetDate.withDayOfMonth(1).plusMonths(1);
      }
      isToday = isToday(curDay, value);

      DateTime newDay = curDay.withDayOfMonth(value);
      String dayCode = getDayCodeFromDateTime(newDay);
      DayMonthly day = new DayMonthly(value, isThisMonth, isToday, dayCode,
          newDay.getWeekOfWeekyear(), new ArrayList<Event>(), i);
      days.add(day);
      value++;
    }
    monthlyCalendar.updateMonthlyCalendar(context, getMonthName(), days, false, mTargetDate);
  }

  public void getDays() {
    Calendar currentCal = Calendar.getInstance();
    List<DayMonthly> days = new ArrayList<DayMonthly>(DAYS_CNT);
    int currMonthDays = currentCal.getMaximum(Calendar.DAY_OF_MONTH);
    int firstDayIndex = mTargetDate.withDayOfMonth(1).getDayOfWeek();
    if (Calendar.getInstance().getFirstDayOfWeek() != Calendar.SUNDAY) {
      firstDayIndex -= 1;
    }
    int prevMonthDays = mTargetDate.minusMonths(1).dayOfMonth().getMaximumValue();

    boolean isThisMonth = false;
    boolean isToday;
    int value = prevMonthDays - firstDayIndex + 1;
    DateTime curDay = mTargetDate;

    for (int i = 0; i < DAYS_CNT; i++) {
      if (firstDayIndex > i) {
        isThisMonth = false;
        curDay = mTargetDate.withDayOfMonth(1).minusMonths(1);
      } else if (firstDayIndex == i) {
        value = 1;
        isThisMonth = true;
        curDay = mTargetDate;
      } else if (value == currMonthDays + 1) {
        value = 1;
        isThisMonth = false;
        curDay = mTargetDate.withDayOfMonth(1).plusMonths(1);
      }
      isToday = isToday(curDay, value);

      DateTime newDay = curDay.withDayOfMonth(value);
      String dayCode = getDayCodeFromDateTime(newDay);
      DayMonthly day = new DayMonthly(value, isThisMonth, isToday, dayCode,
          newDay.getWeekOfWeekyear(), new ArrayList<Event>(), i);
      days.add(day);
      value++;
    }
  }

  private boolean isToday(DateTime targetDate, int curDayInMonth) {
    int targetMonthDays = targetDate.dayOfMonth().getMaximumValue();
    return targetDate.withDayOfMonth(Math.min(curDayInMonth, targetMonthDays))
        .toString(Constant.DAYCODE_PATTERN).equals(mToday);
  }

  private String getMonthName() {
    String month = getMonthName(context, mTargetDate.getMonthOfYear());
    String targetYear = mTargetDate.toString(YEAR_PATTERN);
    if (!targetYear.equals(new DateTime().toString(YEAR_PATTERN))) {
      month += targetYear;
    }
    return month;
  }

  // use manually translated month names, as DateFormat and Joda have issues with a lot of languages
  private String getMonthName(Context context, int id) {
    return context.getResources().getStringArray(R.array.months)[id - 1];
  }

  private String getDayCodeFromDateTime(DateTime dateTime) {
    return dateTime.toString(Constant.DAYCODE_PATTERN);
  }
}
