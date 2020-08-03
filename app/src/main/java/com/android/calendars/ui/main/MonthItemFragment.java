package com.android.calendars.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.calendars.Constant;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.ui.main.customviews.MonthView;
import java.util.Date;
import java.util.List;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthItemFragment extends Fragment implements MonthlyCalendar {

  private MonthlyCalendarImpl mMonthlyCalendarImpl;
  private long mDayCode;
  private MonthView monthView;
  private TextView tvTitle;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mDayCode = getArguments().getLong(Constant.DAY_CODE);
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
    tvTitle = view.findViewById(R.id.top_value);
    mMonthlyCalendarImpl = new MonthlyCalendarImpl(this, view.getContext());
    mMonthlyCalendarImpl.getDays(mDayCode);
  }

  @Override
  public void updateMonthlyCalendar(Context context, String month, List<DayMonthly> days,
      Boolean checkedEvents, Date currTargetDate) {
    tvTitle.setText(month);
    monthView.updateDays(days);
  }
}
