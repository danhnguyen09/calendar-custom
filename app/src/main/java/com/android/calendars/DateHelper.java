package com.android.calendars;

import android.content.Context;
import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Danh Nguyen on 8/4/20.
 */
public class DateHelper {

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
}
