package com.wataandroidteam.calendarcustom;

import android.content.Context;
import androidx.annotation.NonNull;
import com.wataandroidteam.calendarcustom.models.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateHelper {

  public static List<Event> mockEvents() {
    List<Event> events = new ArrayList<>();
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
        createEventDate(9, 8, 2020));
    Event event7 = new Event("007", "7. Event test 07", createEventDate(28, 8, 2020),
        createEventDate(3, 9, 2020));
    Event event8 = new Event("008", "8. Event test 08", createEventDate(6, 8, 2020),
        createEventDate(10, 9, 2020));

    events.add(event1);
    events.add(event2);
    events.add(event3);
    events.add(event4);
    events.add(event5);
    events.add(event6);
//    events.add(event7);
    events.add(event8);
    return events;
  }

  public static String getStringFromDate(@NonNull Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DAYCODE_PATTERN,
        Locale.getDefault());
    return simpleDateFormat.format(date);
  }

  public static boolean isToday(Calendar calendar) {
    Calendar current = Calendar.getInstance();
    return calendar.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)
        && calendar.get(Calendar.MONTH) == current.get(Calendar.MONTH)
        && calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR);
  }

  public static String getMonthName(@NonNull Context context, long targetDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(targetDate);
    return getMonthName(context, calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
  }

  private static String getMonthName(@NonNull Context context, int id) {
    return context.getResources().getStringArray(R.array.months)[id];
  }

  private static Date createEventDate(int day, int month, int year) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.YEAR, year);
    return calendar.getTime();
  }
}
