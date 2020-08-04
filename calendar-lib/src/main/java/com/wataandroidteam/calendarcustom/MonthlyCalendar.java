package com.wataandroidteam.calendarcustom;

import android.content.Context;
import com.wataandroidteam.calendarcustom.models.DayMonthly;
import java.util.List;

public interface MonthlyCalendar {

  void updateMonthlyCalendar(Context context, String month, List<DayMonthly> days);
}
