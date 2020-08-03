package com.android.calendars.ui.main;

import android.os.Bundle;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.android.calendars.Constant;
import java.util.List;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthPagerAdapter extends FragmentStatePagerAdapter {

  private SparseArray<MonthItemFragment> mFragments = new SparseArray<>();
  private List<String> mCodes;

  public MonthPagerAdapter(@NonNull FragmentManager fm, List<String> codes, int behavior) {
    super(fm, behavior);
    mCodes = codes;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    Bundle bundle = new Bundle();
    bundle.putString(Constant.DAY_CODE, mCodes.get(position));
    MonthItemFragment monthFragment = new MonthItemFragment();
    monthFragment.setArguments(bundle);
    mFragments.put(position, monthFragment);
    return monthFragment;
  }

  @Override
  public int getCount() {
    return mCodes != null ? mCodes.size() : 0;
  }
}
