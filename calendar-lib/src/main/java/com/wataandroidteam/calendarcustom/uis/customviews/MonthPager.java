package com.wataandroidteam.calendarcustom.uis.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.duolingo.open.rtlviewpager.RtlViewPager;

public class MonthPager extends RtlViewPager {

  public MonthPager(Context context) {
    super(context);
  }

  public MonthPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
      return super.onInterceptTouchEvent(ev);
    } catch (Exception ignored) {
      return false;
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    try {
      return super.onTouchEvent(ev);
    } catch (Exception ignored) {
      return false;
    }
  }
}
