package com.android.calendars.ui.main.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.calendars.R;
import com.android.calendars.models.DayMonthly;
import com.android.calendars.models.Event;
import com.android.calendars.models.MonthEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Danh Nguyen on 7/31/20.
 */
public class MonthView extends View {

  private float BG_CORNER_RADIUS = 8f;
  private int ROW_COUNT = 6;
  private int COL_COUNT = 7;

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
  private List<MonthEvent> allEvents = new ArrayList<>();
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
    if (days == null || days.isEmpty()) {
      return;
    }
    int curId = 0;
    for (int y = 0; y < ROW_COUNT; y++) {
      for (int x = 0; x < COL_COUNT; x++) {
        DayMonthly day = days.get(curId);
        if (day != null) {
          dayVerticalOffsets.put(day.getIndexOnMonthView(),
              dayVerticalOffsets.get(day.getIndexOnMonthView()) + weekDaysLetterHeight);
          int verticalOffset = dayVerticalOffsets.get(day.getIndexOnMonthView());
          float xPos = x * dayWidth + horizontalOffset;
          float yPos = y * dayHeight + verticalOffset;
          float xPosCenter = xPos + dayWidth / 2;
          float textSize = paint.getTextSize();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(day.getCode());
          Log.d("Today", "day:" + day.getValue() + "/" + (calendar.get(Calendar.MONTH) + 1));
          if (day.isToday()) {
            canvas.drawCircle(xPosCenter, yPos + textSize * 1.65f, textSize * 1.2f,
                getCirclePaint(day));
          }
          canvas.drawText(String.valueOf(day.getValue()), xPosCenter,
              yPos + textSize * 2f, getTextPaint(day));
          dayVerticalOffsets
              .put(day.getIndexOnMonthView(), (int) (verticalOffset + paint.getTextSize() * 2));
        }
        curId++;
      }
    }
    for (MonthEvent event : allEvents) {
      drawEvent(event, canvas);
    }
  }

  public void updateDays(List<DayMonthly> newDays) {
    days = newDays;
    showWeekNumbers = false;
    horizontalOffset = 0;
    initWeekDayLetters();
    setupCurrentDayOfWeekIndex();
    groupAllEvents();
    invalidate();
  }

  public int getWeekDaysLetterHeight() {
    return weekDaysLetterHeight;
  }

  public float getDayWidth() {
    return dayWidth;
  }

  public float getDayHeight() {
    return dayHeight;
  }

  private void drawEvent(MonthEvent event, Canvas canvas) {
    float verticalOffset = 0f;
    int loopSize = Math.min(event.getDayCount(), 7 - event.getStartDayIndex() % 7);
    for (int i = 0; i < loopSize; i++) {
      verticalOffset = Math
          .max(verticalOffset, dayVerticalOffsets.get(event.getStartDayIndex() + i));
    }
    float xPos = event.getStartDayIndex() % 7 * dayWidth + horizontalOffset;
    float yPos = (event.getStartDayIndex() / 7f) * dayHeight;
    float xPosCenter = xPos + dayWidth / 2;

    if (verticalOffset - eventTitleHeight * 2 > dayHeight) {
      canvas.drawText("...", xPosCenter, yPos + verticalOffset - eventTitleHeight / 2f,
          getTextPaint(days.get(event.getStartDayIndex())));
      return;
    }

    // event background rectangle
    float backgroundY = yPos + verticalOffset;
    float bgLeft = xPos + smallPadding;
    float bgTop = backgroundY + smallPadding - eventTitleHeight;
    float bgRight = xPos - smallPadding + dayWidth * event.getDayCount();
    float bgBottom = backgroundY + smallPadding * 2;
    if (bgRight > canvas.getWidth() * 1f) {
      bgRight = canvas.getWidth() * 1f - smallPadding;
      int newStartDayIndex = (event.getStartDayIndex() / 7 + 1) * 7;
      if (newStartDayIndex < 42) {
        MonthEvent newEvent = new MonthEvent(event.getId(), event.getTitle(), event.getStartDate(),
            event.getColor(), newStartDayIndex,
            event.getDayCount() - newStartDayIndex + event.getStartDayIndex(),
            event.getOriginalStartDayIndex(), event.isAllDay());
        drawEvent(newEvent, canvas);
      }
    }

    DayMonthly startDayIndex = days.get(event.getOriginalStartDayIndex());
    DayMonthly endDayIndex = days
        .get(Math.min(event.getStartDayIndex() + event.getDayCount() - 1, 41));
    bgRectF.set(bgLeft, bgTop, bgRight, bgBottom);
    canvas.drawRoundRect(bgRectF, BG_CORNER_RADIUS, BG_CORNER_RADIUS,
        getEventBackgroundColor(event, startDayIndex, endDayIndex));

    drawEventTitle(event, canvas, xPos, yPos + verticalOffset, bgRight - bgLeft - smallPadding,
        startDayIndex, endDayIndex);

    for (int i = 0; i < loopSize; i++) {
      dayVerticalOffsets.put(event.getStartDayIndex() + i,
          (int) (verticalOffset + eventTitleHeight + smallPadding * 2));
    }
  }

  private void drawEventTitle(MonthEvent event, Canvas canvas, float x, float y,
      float availableWidth, DayMonthly startDay, DayMonthly endDay) {
    CharSequence ellipsized = TextUtils
        .ellipsize(event.getTitle(), eventTitlePaint, availableWidth - smallPadding,
            TextUtils.TruncateAt.END);
    canvas.drawText(event.getTitle(), 0, ellipsized.length(), x + smallPadding * 2, y,
        getEventTitlePaint(event, startDay, endDay));
  }

  private void groupAllEvents() {
    days.forEach(day -> {
      List<Event> events = day.getDayEvents();
      if (events != null && !events.isEmpty()) {
        events.forEach(event -> {
          MonthEvent lastEvent = allEvents.stream()
              .filter(evt -> event.getId().equalsIgnoreCase(evt.getId())).findFirst().orElse(null);
          int dayEventCount = getEventLastingDaysCount(event);
          boolean validDayEvent = false;
          if ((lastEvent == null || lastEvent.getStartDayIndex() + dayEventCount <= day
              .getIndexOnMonthView()) && !validDayEvent) {
            MonthEvent monthEvent = new MonthEvent(event.getId(), event.getTitle(),
                event.getStartDate(), Color.parseColor("#903BAA"), day.getIndexOnMonthView(), dayEventCount,
                day.getIndexOnMonthView(), false);
            allEvents.add(monthEvent);
          }
        });
      }
    });
  }

  // take into account cases when an event starts on the previous screen, subtract those days
  private int getEventLastingDaysCount(Event event) {
    Date eventStartDateTime = event.getStartDate();
    Date eventEndDateTime = event.getEndDate();
    Date screenStartDateTime = days.get(0).getCode();
    int diff = (int) ((eventStartDateTime.getTime() - screenStartDateTime.getTime()) / (1000 * 60
        * 60 * 24));
    if (diff < 0) {
      eventStartDateTime = screenStartDateTime;
    }
    Calendar midNightCal = Calendar.getInstance();
    midNightCal.setTime(eventEndDateTime);
    midNightCal.set(Calendar.HOUR_OF_DAY, 0);
    midNightCal.set(Calendar.MINUTE, 0);
    midNightCal.set(Calendar.SECOND, 0);
    midNightCal.set(Calendar.MILLISECOND, 0);

    boolean isMidnight = midNightCal.getTimeInMillis() == eventStartDateTime.getTime();
    int numDayOfEvent = (int) ((eventEndDateTime.getTime() - eventStartDateTime.getTime()) / (1000
        * 60
        * 60 * 24));
    int daysCnt = numDayOfEvent;
    if (numDayOfEvent == 1 && isMidnight) {
      daysCnt = 0;
    }
    return daysCnt + 1;
  }

  private void initWeekDayLetters() {
    dayLetters = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.week_day_letters)));
    if (Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY) {
      int size = dayLetters.size();
      String last = dayLetters.remove(size - 1);
      dayLetters.add(0, last);
    }
  }

  private Paint getEventTitlePaint(MonthEvent event, DayMonthly startDay, DayMonthly endDay) {
    int paintColor = paintColor = Color.WHITE;;
//    if (!startDay.isThisMonth() && !endDay.isThisMonth()) {
//      paintColor = Color.WHITE;
//    }
    Paint curPaint = new Paint(eventTitlePaint);
    curPaint.setColor(paintColor);
    return curPaint;
  }

  private Paint getEventBackgroundColor(MonthEvent event, DayMonthly startDay, DayMonthly endDay) {
    int paintColor = event.getColor();
    if (!startDay.isThisMonth() && !endDay.isThisMonth()) {
      paintColor = Color.GREEN;
    }
    return getColoredPaint(paintColor);
  }

  private Paint getTextPaint(DayMonthly startDay) {
    int paintColor = textColor;
    if (startDay.isToday()) {
      paintColor = Color.WHITE;
    } else if (!startDay.isThisMonth()) {
      paintColor = Color.LTGRAY;
    }
    return getColoredPaint(paintColor);
  }

  private Paint getCirclePaint(DayMonthly day) {
    Paint curPaint = new Paint(paint);
    int paintColor = Color.RED;
    if (!day.isThisMonth()) {
      paintColor = Color.LTGRAY;
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
      canvas.drawLine(lineX, weekDaysLetterHeight, lineX, canvas.getHeight(), gridPaint);
    }

    // horizontal lines
//    canvas.drawLine(0f, 0f, canvas.getHeight(), 0f, gridPaint);
    for (int i = 0; i <= 5; i++) {
      canvas.drawLine(0f, i * dayHeight + weekDaysLetterHeight, canvas.getWidth(),
          i * dayHeight + weekDaysLetterHeight, gridPaint);
    }
  }

  private void init(Context context) {
    primaryColor = Color.BLACK;
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
    currDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    if (Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY) {
      currDayOfWeek %= 7;
    } else {
      currDayOfWeek--;
    }
  }
}
