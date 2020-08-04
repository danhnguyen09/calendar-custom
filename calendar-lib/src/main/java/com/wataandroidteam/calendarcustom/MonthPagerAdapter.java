package com.wataandroidteam.calendarcustom;

import android.os.Bundle;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wataandroidteam.calendarcustom.uis.MonthItemFragment;
import java.util.List;

public class MonthPagerAdapter extends FragmentStatePagerAdapter {

  private SparseArray<MonthItemFragment> mFragments = new SparseArray<>();
  private List<Long> mCodes;

  public MonthPagerAdapter(@NonNull FragmentManager fm, List<Long> codes, int behavior) {
    super(fm, behavior);
    mCodes = codes;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    Bundle bundle = new Bundle();
    bundle.putLong(Constant.DAY_CODE, mCodes.get(position));
    MonthItemFragment monthFragment = MonthItemFragment.newInstance(bundle);
    mFragments.put(position, monthFragment);
    return monthFragment;
  }

  @Override
  public int getItemPosition(@NonNull Object object) {
    return POSITION_NONE;
  }

  @Override
  public int getCount() {
    return mCodes != null ? mCodes.size() : 0;
  }
}
