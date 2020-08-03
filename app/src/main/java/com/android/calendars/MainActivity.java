package com.android.calendars;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.calendars.ui.main.MonthFragment;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MonthFragment monthFragment = new MonthFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragments_holder, monthFragment, monthFragment.getClass().getSimpleName())
        .addToBackStack(null)
        .commit();
  }

  @Override
  public void onBackPressed() {
    int stackCount = getSupportFragmentManager().getBackStackEntryCount();
    if (stackCount < 2) {
      finish();
    } else {
      super.onBackPressed();
    }
  }
}