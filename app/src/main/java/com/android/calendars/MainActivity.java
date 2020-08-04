package com.android.calendars;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.wataandroidteam.calendarcustom.DateHelper;
import com.wataandroidteam.calendarcustom.uis.MonthFragment;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MonthFragment monthFragment = MonthFragment.newInstance();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragments_holder, monthFragment, monthFragment.getClass().getSimpleName())
        .addToBackStack(null)
        .commit();
    findViewById(R.id.fab)
        .setOnClickListener(view -> monthFragment.onAddEvents(DateHelper.mockEvents()));
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