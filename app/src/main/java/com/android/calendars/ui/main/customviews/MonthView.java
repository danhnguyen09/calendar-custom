package com.android.calendars.ui.main.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthView extends View {

  private float BG_CORNER_RADIUS = 8f;
  private int ROW_COUNT = 6;

  private Paint paint;
  private TextPaint eventTitlePaint;
  private Paint gridPaint;
  //  private var config = context.config
  private float dayWidth = 0f;
  private float dayHeight = 0f;
  private int primaryColor = 0;
  private int textColor = 0;
  private int weekDaysLetterHeight = 0;
  private int eventTitleHeight = 0;
  private int currDayOfWeek = 0;
  private float smallPadding = 0f;
  private int maxEventsPerDay = 0;
  private int horizontalOffset = 0;
  private boolean showWeekNumbers = false;
  private boolean dimPastEvents = true;
  //  private var allEvents = ArrayList<MonthViewEvent>()
  private RectF bgRectF = new RectF();
  private List<String> dayLetters = new ArrayList<>();
  private List<DayMonthly> days = new ArrayList<>();
  private SparseIntArray dayVerticalOffsets = new SparseIntArray();

  public MonthView(Context context) {
    super(context);
    init(context);
  }

  public MonthView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    dayVerticalOffsets.clear();
    measureDaySize(canvas);
    drawGrid(canvas);
    addWeekDayLetters(canvas);
    if (days == null || days.isEmpty()) return;
    int curId = 0;
    for (int y = 0; y< ROW_COUNT; y ++) {
      for (int x = 0; x <= 6; x++) {
        DayMonthly day = days.get(curId);
        if (day != null) {
          dayVerticalOffsets.put(day.getIndexOnMonthView(), dayVerticalOffsets.get(day.getIndexOnMonthView()) + weekDaysLetterHeight);
          int verticalOffset = dayVerticalOffsets.get(day.getIndexOnMonthView());
          float xPos = x * dayWidth + horizontalOffset;
          float yPos = y * dayHeight + verticalOffset;
          float xPosCenter = xPos + dayWidth / 2;
          float textSize = paint.getTextSize();
          if (day.isToday()) {
            canvas.drawCircle(xPosCenter, yPos + textSize * 1.15f, textSize * 0.7f, getCirclePaint(day));
          }
          canvas.drawText(String.valueOf(day.getValue()), xPosCenter, yPos + textSize + textSize * 0.5f, getTextPaint(day));
          dayVerticalOffsets.put(day.getIndexOnMonthView(), (int) (verticalOffset + paint.getTextSize() * 2));
        }
        curId++;
      }
    }
  }

  public void updateDays(List<DayMonthly> newDays) {
    days = newDays;
    showWeekNumbers = false;
    horizontalOffset = 0;
    initWeekDayLetters();
    setupCurrentDayOfWeekIndex();
//    groupAllEvents();
    invalidate();
  }

  private void initWeekDayLetters() {
    dayLetters = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.week_day_letters)));
    if (Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY) {
      int size = dayLetters.size();
      String last = dayLetters.remove(size - 1);
      dayLetters.add(0, last);
    }
  }

  private Paint getTextPaint(DayMonthly startDay) {
    int paintColor = textColor;
    if (startDay.isToday()) {
      paintColor = Color.RED;
    }

    if (!startDay.isThisMonth()) {
      paintColor = Color.DKGRAY;
    }

    return getColoredPaint(paintColor);
  }

  private Paint getCirclePaint(DayMonthly day) {
    Paint curPaint = new Paint(paint);
    int paintColor = primaryColor;
    if (!day.isThisMonth()) {
      paintColor = Color.GRAY;
    }
    curPaint.setColor(paintColor);
    return curPaint;
  }

  private void addWeekDayLetters(Canvas canvas) {
    for (int i = 0; i <= 6; i++) {
      float xPos = horizontalOffset + (i + 1) * dayWidth - dayWidth / 2;
      Paint weekDayLetterPaint = paint;
      if (i == currDayOfWeek) {
        weekDayLetterPaint = getColoredPaint(primaryColor);
      }
      canvas.drawText(dayLetters.get(i), xPos, weekDaysLetterHeight * 0.7f, weekDayLetterPaint);
    }
  }

  private Paint getColoredPaint(int color) {
    Paint curPaint = new Paint(paint);
    curPaint.setColor(color);
    return curPaint;
  }

  private void measureDaySize(Canvas canvas) {
    dayWidth = (canvas.getWidth() - horizontalOffset) / 7f;
    dayHeight = (canvas.getHeight() - weekDaysLetterHeight) / (ROW_COUNT * 1f);
    float availableHeightForEvents = (dayHeight - weekDaysLetterHeight) * 1f;
    maxEventsPerDay = (int) (availableHeightForEvents / eventTitleHeight);
  }

  private void drawGrid(Canvas canvas) {
    // vertical lines
    for (int i = 0; i <= 6; i++) {
      float lineX = i * dayWidth;
      if (showWeekNumbers) {
        lineX += horizontalOffset;
      }
      canvas.drawLine(lineX, 0f, lineX, canvas.getHeight(), gridPaint);
    }

    // horizontal lines
    canvas.drawLine(0f, 0f, canvas.getHeight(), 0f, gridPaint);
    for (int i = 0; i <= 5; i++) {
      canvas.drawLine(0f, i * dayHeight + weekDaysLetterHeight, canvas.getWidth(),
          i * dayHeight + weekDaysLetterHeight, gridPaint);
    }
  }

  private void init(Context context) {
    primaryColor = Color.GREEN;
    textColor = Color.BLACK;
//    showWeekNumbers = config.showWeekNumbers
//    dimPastEvents = config.dimPastEvents

    smallPadding = context.getResources().getDisplayMetrics().density;
    float normalTextSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_14sp);
    weekDaysLetterHeight = (int) (normalTextSize * 2);
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(textColor);
    paint.setTextSize(normalTextSize);
    paint.setTextAlign(Paint.Align.CENTER);

    gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    gridPaint.setColor(Color.GRAY);

    float smallerTextSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_10sp);
    eventTitleHeight = (int) smallerTextSize;
    eventTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    eventTitlePaint.setColor(textColor);
    eventTitlePaint.setTextSize(smallerTextSize);
    eventTitlePaint.setTextAlign(Paint.Align.LEFT);
    dayLetters = Arrays.asList(context.getResources().getStringArray(R.array.week_day_letters));
    setupCurrentDayOfWeekIndex();
  }

  private void setupCurrentDayOfWeekIndex() {
    if (days == null || days.isEmpty() || (days.get(0).isThisMonth() && days.get(0).isToday())) {
      currDayOfWeek = -1;
      return;
    }
    currDayOfWeek = new DateTime().getDayOfWeek();
    if (Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY) {
      currDayOfWeek %= 7;
    } else {
      currDayOfWeek--;
    }
  }
}
