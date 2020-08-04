package com.wataandroidteam.calendarcustom.uis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.wataandroidteam.calendarcustom.IMonthListener;
import com.wataandroidteam.calendarcustom.MonthPagerAdapter;
import com.wataandroidteam.calendarcustom.R;
import com.wataandroidteam.calendarcustom.models.DayMonthly;
import com.wataandroidteam.calendarcustom.models.Event;
import com.wataandroidteam.calendarcustom.uis.customviews.MonthPager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment implements IMonthListener {

  private int NUMBER_OF_MONTH_IN_THE_PAST = 20;
  private int NUMBER_OF_MONTH_IN_THE_FEATURE = 100;
  private MonthPager mMonthPager;
  private MonthPagerAdapter mMonthPagerAdapter;
  private List<Event> mEvents;

  public static MonthFragment newInstance() {
    return new MonthFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_month, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    mMonthPager = view.findViewById(R.id.fragment_months_viewpager);
    mMonthPagerAdapter = new MonthPagerAdapter(getChildFragmentManager(), getMonths(), 0);
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

  @Override
  public void onAddEvents(List<Event> events) {
    mEvents = events;
    mMonthPagerAdapter.notifyDataSetChanged();
  }

  @Override
  public List<Event> getEventList() {
    return mEvents;
  }

  @Override
  public void onDayMonthlyClicked(DayMonthly day) {
    Toast.makeText(getContext(), day.getCode().toString(), Toast.LENGTH_SHORT).show();
  }
}
