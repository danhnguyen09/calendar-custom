package com.android.calendars.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.calendars.Constant;
import com.android.calendars.R;
import com.android.calendars.ui.main.customviews.MonthPager;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class MonthFragment extends Fragment {

  private int PREFILLED_MONTHS = 2;
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
    mMonthPagerAdapter = new MonthPagerAdapter(getChildFragmentManager(), getMonths(getTodayCode()), 0);
    mMonthPager.setAdapter(mMonthPagerAdapter);
  }

  private List<String> getMonths(String code) {
    List<String> months = new ArrayList<>(PREFILLED_MONTHS);
    DateTime today = getDateTimeFromCode(code).withDayOfMonth(1);
    for (int i = -PREFILLED_MONTHS / 2; i <= PREFILLED_MONTHS / 2; i++) {
      months.add(getDayCodeFromDateTime(today.plusMonths(i)));
    }
    return months;
  }

  private DateTime getDateTimeFromCode(String dayCode) {
    return DateTimeFormat.forPattern(Constant.DAYCODE_PATTERN).withZone(DateTimeZone.UTC)
        .parseDateTime(dayCode);
  }

  private String getDayCodeFromDateTime(DateTime dateTime) {
    return dateTime.toString(Constant.DAYCODE_PATTERN);
  }

  private String getDayCodeFromTS(long ts) {
    String dayCode = getDateTimeFromTS(ts).toString(Constant.DAYCODE_PATTERN);
    if (!TextUtils.isEmpty(dayCode)) {
      return dayCode;
    } else {
      return "0";
    }
  }

  private DateTime getDateTimeFromTS(long ts) {
    return new DateTime(ts, DateTimeZone.getDefault());
  }

  private String getTodayCode() {
    return getDayCodeFromTS(System.currentTimeMillis());
  }
}
