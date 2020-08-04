package com.android.calendars.ui.main;

import android.content.Context;
import com.android.calendars.models.DayMonthly;
import java.util.Date;
import java.util.List;

public interface MonthlyCalendar {

  void updateMonthlyCalendar(Context context, String month, List<DayMonthly> days);
}
