package com.android.calendars.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.calendars.Constant;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.ui.main.customviews.MonthView;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthItemFragment extends Fragment implements MonthlyCalendar {

  private MonthlyCalendarImpl mMonthlyCalendarImpl;
  private String mDayCode;
  private MonthView monthView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mDayCode = getArguments().getString(Constant.DAY_CODE);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_month_item, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    monthView = view.findViewById(R.id.month_view_wrapper);
    mMonthlyCalendarImpl = new MonthlyCalendarImpl(this, view.getContext());
    DateTime targetDate = getDateTimeFromCode(mDayCode);
    mMonthlyCalendarImpl.setTargetDate(targetDate);
    mMonthlyCalendarImpl.getDays(false);
  }

  @Override
  public void updateMonthlyCalendar(Context context, String month, List<DayMonthly> days,
      Boolean checkedEvents, DateTime currTargetDate) {
    monthView.updateDays(days);
  }

  private DateTime getDateTimeFromCode(String dayCode) {
    return DateTimeFormat.forPattern(Constant.DAYCODE_PATTERN)
        .parseDateTime(dayCode);
  }
}
