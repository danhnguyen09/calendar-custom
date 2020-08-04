package com.android.calendars.ui.main.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.calendars.DateHelper;
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

  private float BG_CORNER_RADIUS = 6f;
  private int ROW_COUNT = 6;
  private int COL_COUNT = 7;

  private Paint paint;
  private TextPaint eventTitlePaint;
  private Paint gridPaint;
  private float dayWidth = 0f;
  private float dayHeight = 0f;
  private int primaryColor = 0;
  private int textColor = 0;
  private int weekDaysLetterHeight = 0;
  private int eventTitleHeight = 0;
  private int currDayOfWeek = 0;
  private float smallPadding = 0f;
  private int horizontalOffset = 0;
  private List<MonthEvent> allEvents = new ArrayList<>();
  private RectF bgRectF = new RectF();
  private List<String> dayLetters = new ArrayList<>();
  private List<DayMonthly> days = new ArrayList<>();
  private SparseIntArray dayVerticalOffsets = new SparseIntArray();

  private final int BASE_BLUE = Color.parseColor("#007AFF");
  private final int LINE_GRAY = Color.parseColor("#D4D4D4");

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
    if (dayHeight == 0 && dayWidth == 0) {
      return;
    }
    //draw month UI and day of month
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
//          Calendar calendar = Calendar.getInstance();
//          calendar.setTime(day.getCode());
//          Log.d("Today", "day:" + day.getValue() + "/" + (calendar.get(Calendar.MONTH) + 1));
          if (day.isToday()) {
            canvas.drawCircle(xPosCenter, yPos + textSize * 1.65f, textSize * 1.2f,
                getCirclePaint(day));
          }
          canvas.drawText(String.valueOf(day.getValue()), xPosCenter,
              yPos + textSize * 2f, getTextPaint(day));
          dayVerticalOffsets
              .put(day.getIndexOnMonthView(), (int) (verticalOffset + textSize * 2f));
        }
        curId++;
      }
    }
    //draw events
    for (MonthEvent event : allEvents) {
      drawEvent(event, canvas);
    }
  }

  public void updateDays(List<DayMonthly> newDays) {
    days = newDays;
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
    int numberDayEvent = event.getDayCount();
    float xPos = event.getStartDayIndex() % 7 * dayWidth + horizontalOffset;
    float yPos = (int) (event.getStartDayIndex() / 7f) * dayHeight + paint.getTextSize();
    float xPosCenter = xPos + dayWidth / 2;

    if (verticalOffset > dayHeight) {
      canvas.drawText("...", xPosCenter, yPos + verticalOffset - eventTitleHeight * 0.5f,
          getTextPaint(days.get(event.getStartDayIndex())));
      return;
    }

    float backgroundY = yPos + verticalOffset;
    float bgLeft = xPos + smallPadding;
    float bgRight = xPos + dayWidth * numberDayEvent - smallPadding;
    float bgTop = backgroundY - eventTitleHeight;
    float bgBottom = bgTop + eventTitleHeight + smallPadding * 4;

    boolean isOneEventOnDay = event.getNumberEventOnDay() == 1;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(event.getStartDate());
    boolean isToday = DateHelper.isToday(calendar);

    if (isToday) {
      bgTop += eventTitleHeight;
      backgroundY += eventTitleHeight;
    }
    if (isOneEventOnDay) {
      if (isToday) {
        bgBottom += eventTitleHeight * 3f;
      } else {
        bgBottom += eventTitleHeight * 2f;
      }
    }
    if (bgRight > canvas.getWidth() * 1f) {
      bgRight = canvas.getWidth() * 1f - smallPadding;
      int newStartDayIndex = (event.getStartDayIndex() / 7 + 1) * 7;
      if (newStartDayIndex < 42) {
        MonthEvent newEvent = new MonthEvent(event.getId(), event.getTitle(), event.getStartDate(),
            newStartDayIndex, event.getDayCount() - newStartDayIndex + event.getStartDayIndex(),
            event.getOriginalStartDayIndex(), event.isAllDay());
        newEvent.setNumberEventOnDay(event.getNumberEventOnDay());
        newEvent.setEventBottomColor(event.getEventBottomColor());
        drawEvent(newEvent, canvas);
      }
    }
    //draw background
    bgRectF.set(bgLeft, bgTop, bgRight, bgBottom);
    canvas.drawRoundRect(bgRectF, BG_CORNER_RADIUS, BG_CORNER_RADIUS,
        getEventBackgroundColor(event));

    drawPathLeftOrBottom(canvas, event, bgLeft, bgTop, bgRight, bgBottom);
    drawEventTitle(event, canvas, xPos, backgroundY, bgRight - bgLeft - smallPadding);

    for (int i = 0; i < loopSize; i++) {
      dayVerticalOffsets.put(event.getStartDayIndex() + i,
          (int) (verticalOffset + bgRectF.height() + smallPadding));
    }
  }

  private void drawPathLeftOrBottom(Canvas canvas, MonthEvent event, float bgLeft, float bgTop,
      float bgRight, float bgBottom) {
    int color = BASE_BLUE;
    RectF rectLeft = new RectF();
    rectLeft.set(bgLeft, bgTop, bgLeft + smallPadding, bgBottom);
    final Path path = new Path();
    float[] cornersTopLeft = new float[]{
        BG_CORNER_RADIUS, BG_CORNER_RADIUS,        // Top left radius in px
        0, 0,                                      // Top right radius in px
        0, 0,                                      // Bottom right radius in px
        BG_CORNER_RADIUS, BG_CORNER_RADIUS         // Bottom left radius in px
    };

    if (event.getNumberEventOnDay() < 2) {
      rectLeft.set(bgLeft, bgBottom - smallPadding, bgRight, bgBottom);
      cornersTopLeft = new float[]{
          0, 0,        // Top left radius in px
          0, 0,                                      // Top right radius in px
          BG_CORNER_RADIUS, BG_CORNER_RADIUS,        // Bottom right radius in px
          BG_CORNER_RADIUS, BG_CORNER_RADIUS         // Bottom left radius in px
      };
    }
    path.addRoundRect(rectLeft, cornersTopLeft, Path.Direction.CW);
    Paint leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    leftPaint.setColor(color);
    canvas.drawPath(path, leftPaint);
  }

  private void drawEventTitle(MonthEvent event, Canvas canvas, float x, float y,
      float availableWidth) {
    Paint paint = getEventTitlePaint();
    if (event.getTitle().contains("\n") && event.getNumberEventOnDay() == 1) {
      String[] text = event.getTitle().split("\n");
      for (String s : text) {
        CharSequence ellipsized = TextUtils
            .ellipsize(s, eventTitlePaint, availableWidth - smallPadding,
                TextUtils.TruncateAt.END);
        canvas.drawText(s, 0, ellipsized.length(), x + smallPadding * 2, y, paint);
        y += paint.getTextSize();
      }
    } else {
      CharSequence ellipsized = TextUtils
          .ellipsize(event.getTitle(), eventTitlePaint, availableWidth - smallPadding,
              TextUtils.TruncateAt.END);
      float textOffset = (bgRectF.height() - eventTitleHeight - smallPadding) * 0.5f;
      canvas.drawText(event.getTitle(), 0, ellipsized.length(), x + smallPadding * 2,
          y + textOffset, paint);
    }
  }

  private void groupAllEvents() {
    days.forEach(day -> {
      List<Event> events = day.getDayEvents();
      if (events != null && !events.isEmpty()) {
        events.forEach(event -> {
          MonthEvent lastEvent = allEvents.stream()
              .filter(evt -> event.getId().equalsIgnoreCase(evt.getId())).findFirst().orElse(null);
          int dayEventCount = getEventLastingDaysCount(event);
          if ((lastEvent == null || lastEvent.getStartDayIndex() + dayEventCount <= day
              .getIndexOnMonthView())) {
            MonthEvent monthEvent = new MonthEvent(event.getId(), event.getTitle(),
                event.getStartDate(), day.getIndexOnMonthView(),
                dayEventCount,
                day.getIndexOnMonthView(), false);
            monthEvent.setNumberEventOnDay(events.size());
            monthEvent.setEventBottomColor(event.getBottomColors());
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

  private Paint getEventTitlePaint() {
    Paint curPaint = new Paint(eventTitlePaint);
    curPaint.setColor(Color.BLACK);
    curPaint.setTextAlign(Align.LEFT);
    return curPaint;
  }

  private Paint getEventBackgroundColor(MonthEvent event) {
    int paintColor = event.getBackgroundColor();
    return getColoredPaint(paintColor);
  }

  private Paint getTextPaint(DayMonthly startDay) {
    int paintColor = textColor;
    if (startDay.isToday()) {
      paintColor = Color.WHITE;
    } else if (!startDay.isThisMonth()) {
      paintColor = LINE_GRAY;
    }
    return getColoredPaint(paintColor);
  }

  private Paint getCirclePaint(DayMonthly day) {
    Paint curPaint = new Paint(paint);
    int paintColor = BASE_BLUE;
    if (!day.isThisMonth()) {
      paintColor = LINE_GRAY;
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
  }

  private void drawGrid(Canvas canvas) {
    // vertical lines
    for (int i = 0; i <= 6; i++) {
      float lineX = i * dayWidth;
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
    smallPadding = context.getResources().getDimensionPixelSize(R.dimen.padding_2dp);
    float normalTextSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_14sp);
    weekDaysLetterHeight = (int) (normalTextSize * 2);
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(textColor);
    paint.setTextSize(normalTextSize);
    paint.setTextAlign(Paint.Align.CENTER);

    gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    gridPaint.setColor(LINE_GRAY);
    gridPaint.setStrokeWidth(2f);

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
