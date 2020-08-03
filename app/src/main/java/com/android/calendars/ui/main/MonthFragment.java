package com.android.calendars.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.calendars.R;
import com.android.calendars.ui.main.customviews.MonthPager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment {

  private int NUMBER_OF_MONTH_IN_THE_PAST = 20;
  private int NUMBER_OF_MONTH_IN_THE_FEATURE = 100;
  private MonthPager mMonthPager;
  private MonthPagerAdapter mMonthPagerAdapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_month, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    mMonthPager = view.findViewById(R.id.fragment_months_viewpager);
    mMonthPagerAdapter = new MonthPagerAdapter(getChildFragmentManager(), getMonths(),
        0);
    mMonthPager.setAdapter(mMonthPagerAdapter);
    mMonthPager.setCurrentItem(NUMBER_OF_MONTH_IN_THE_PAST);
  }

  private List<Long> getMonths() {
    List<Long> months = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    for (int i = -NUMBER_OF_MONTH_IN_THE_PAST; i <= NUMBER_OF_MONTH_IN_THE_FEATURE; i++) {
      calendar.setTimeInMillis(System.currentTimeMillis());
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.add(Calendar.MONTH, i);
      months.add(calendar.getTimeInMillis());
    }
    return months;
  }
}
