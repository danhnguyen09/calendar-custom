package com.android.calendars.ui.main;

import android.content.Context;
import com.android.calendars.models.DayMonthly;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public interface MonthlyCalendar {

  void updateMonthlyCalendar(Context context, String month, List<DayMonthly> days,
      Boolean checkedEvents, DateTime currTargetDate);
}
