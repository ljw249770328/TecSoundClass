package com.example.administrator.tecsoundclass.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class CustomDatePicker {

    public CustomDatePicker(TPDialogFactory tpDialogFactory, String 请选择日期, ResultHandler resultHandler, String startDate, String time) {
    }

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String time);
    }

    public enum SCROLL_TYPE {
        HOUR(2),
        MINUTE(1),
        YEAR(0),
        MONTH(1),
        DAY(2);

        SCROLL_TYPE(int value) {
            this.value = value;
        }

        public int value;
    }

    private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
    private int scrollUnits1=SCROLL_TYPE.YEAR.value + SCROLL_TYPE.MONTH.value+ SCROLL_TYPE.DAY.value;
    private ResultHandler handler;
    private Context context;
    private boolean canAccess = false;

    private Dialog datePickerDialog;
    private DatePickerView year_pv, month_pv, day_pv, hour_pv, minute_pv;

    private static final int MAX_MINUTE = 59;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MIN_HOUR = 0;
    private static final int MAX_MONTH = 12;

    private ArrayList<String> year, month, day, hour, minute;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private int lastMonthDays; //上一个被选中的月份天数
    private String title;
    private String currentMon, currentDay, currentHour, currentMin; //当前选中的月、日、时、分
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;
    private Calendar selectedCalender, startCalendar, endCalendar;
    private TextView tv_title, tv_cancle, tv_select,year_text,month_text,day_text, hour_text, minute_text;

    public CustomDatePicker(Context context, String title, ResultHandler resultHandler, String startDate, String endDate) {
        if (isValidDate(startDate, "yyyy-MM-dd HH:mm") && isValidDate(endDate, "yyyy-MM-dd HH:mm")) {
            canAccess = true;
            this.context = context;
            this.handler = resultHandler;
            this.title = title;
            selectedCalender = Calendar.getInstance();
            startCalendar = Calendar.getInstance();
            endCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            try {
                startCalendar.setTime(sdf.parse(startDate));
                endCalendar.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initDialog();
            initView();
        }
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.TimePickerDialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.custom_date_picker);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            lp.windowAnimations=R.style.BottomDialogAnimation;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        year_pv = (DatePickerView) datePickerDialog.findViewById(R.id.year_pv);
        month_pv = (DatePickerView) datePickerDialog.findViewById(R.id.month_pv);
        day_pv = (DatePickerView) datePickerDialog.findViewById(R.id.day_pv);
        hour_pv = (DatePickerView) datePickerDialog.findViewById(R.id.hour_pv);
        minute_pv = (DatePickerView) datePickerDialog.findViewById(R.id.minute_pv);
        tv_title = (TextView) datePickerDialog.findViewById(R.id.tv_title);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) datePickerDialog.findViewById(R.id.tv_select);
        year_text = (TextView) datePickerDialog.findViewById(R.id.year_text);
        month_text = (TextView) datePickerDialog.findViewById(R.id.month_text);
        day_text = (TextView) datePickerDialog.findViewById(R.id.day_text);
        hour_text = (TextView) datePickerDialog.findViewById(R.id.hour_text);
        minute_text = (TextView) datePickerDialog.findViewById(R.id.minute_text);

        tv_title.setText(title);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pat="yyyy-MM-dd HH:mm";
                if (scrollUnits1==0){
                    pat="HH:mm";
                }
                Log.d("------------TAGS--------------",scrollUnits+pat+scrollUnits1);
                SimpleDateFormat sdf = new SimpleDateFormat(pat, Locale.CHINA);
                handler.handle(sdf.format(selectedCalender.getTime()));
                datePickerDialog.dismiss();
            }
        });
    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);
        selectedCalender.setTime(startCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {

            if ((scrollUnits1 & SCROLL_TYPE.YEAR.value) != SCROLL_TYPE.YEAR.value) {
                year.add(formatTimeUnit(startYear));
            } else {
                for (int i = startYear; i <= endYear; i++) {
                    year.add(String.valueOf(i));
                }
            }

            if ((scrollUnits1 & SCROLL_TYPE.MONTH.value) != SCROLL_TYPE.MONTH.value) {
                month.add(formatTimeUnit(startMonth));
            } else {
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits1 & SCROLL_TYPE.DAY.value) != SCROLL_TYPE.DAY.value) {
                day.add(formatTimeUnit(startDay));
            } else {
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            }


            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));

            if ((scrollUnits1 & SCROLL_TYPE.MONTH.value) != SCROLL_TYPE.MONTH.value) {
                month.add(formatTimeUnit(startMonth));
            } else {
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits1 & SCROLL_TYPE.DAY.value) != SCROLL_TYPE.DAY.value) {
                day.add(formatTimeUnit(startDay));
            } else {
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));

            if ((scrollUnits1 & SCROLL_TYPE.DAY.value) != SCROLL_TYPE.DAY.value) {
                day.add(formatTimeUnit(startDay));
            } else {
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));
            hour.add(formatTimeUnit(startHour));

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        }
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
    }

    private void loadComponent() {
        year_pv.setData(year);
        month_pv.setData(month);
        day_pv.setData(day);
        hour_pv.setData(hour);
        minute_pv.setData(minute);
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        day_pv.setSelected(0);
        hour_pv.setSelected(0);
        minute_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                monthChange();
            }
        });

        month_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                currentMon = text; //保存选择的月份
                dayChange();
            }
        });

        day_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
                currentDay = text;//保存选择的日期
                hourChange();
            }
        });

        hour_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                currentHour = text; //保存选择的小时
                minuteChange();
            }
        });

        minute_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
                currentMin = text; //保存选择的分钟
            }
        });
    }

    private void monthChange() {
        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        }
//        selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        month_pv.setData(month);
        if (month.size() < MAX_MONTH && Integer.valueOf(currentMon) > month.size()) {
            month_pv.setSelected(month.size() - 1);
            selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
            selectedCalender.set(Calendar.MONTH, month.size() - 1);
        } else {
            month_pv.setSelected(currentMon);
            selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
            selectedCalender.set(Calendar.MONTH, Integer.valueOf(currentMon) - 1);
        }
        executeAnimator(month_pv);

        month_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, 100);
    }

    private void dayChange() {
        day.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        }
        day_pv.setData(day);
//        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
//        day_pv.setSelected(0);
        if (day.size() < lastMonthDays && Integer.valueOf(currentDay) > day.size()) {
            day_pv.setSelected(day.size() - 1);
            currentDay = formatTimeUnit(day.size());
        } else {
            day_pv.setSelected(currentDay);
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(currentDay));
        //重新赋值
        lastMonthDays = day.size();
        executeAnimator(day_pv);

        day_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, 100);
    }

    private void hourChange() {
        if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
            hour.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (int i = MIN_HOUR; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
//            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
//            hour_pv.setSelected(0);
            hour_pv.setData(hour);
            if (hour.size() < 24 && Integer.valueOf(currentHour) > hour.size()) {
                hour_pv.setSelected(hour.size() - 1);
                selectedCalender.set(Calendar.HOUR_OF_DAY, hour.size());
                currentHour = formatTimeUnit(hour.size());
            } else {
                hour_pv.setSelected(currentHour);
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.valueOf(currentHour));
            }
            executeAnimator(hour_pv);
        }

        hour_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, 100);
    }

    private void minuteChange() {
        if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
            minute.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
            minute_pv.setData(minute);
//            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
//            minute_pv.setSelected(0);
            if (minute.size() < 60 && minute.size() < Integer.valueOf(currentMin)) {
                minute_pv.setSelected(minute.size() - 1);
                selectedCalender.set(Calendar.MINUTE, minute.size());
                //改变当前选择的分钟
                currentMin = formatTimeUnit(minute.size());
            } else {
                minute_pv.setSelected(currentMin);
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(currentMin));
            }
            executeAnimator(minute_pv);
        }
        executeScroll();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        year_pv.setCanScroll(year.size() > 1 && (scrollUnits1 & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        month_pv.setCanScroll(month.size() > 1 && (scrollUnits1 & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        day_pv.setCanScroll(day.size() > 1 && (scrollUnits1 & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        hour_pv.setCanScroll(hour.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        minute_pv.setCanScroll(minute.size() > 1 && (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
    }

    private int disScrollUnit(SCROLL_TYPE... scroll_types) {
        if (scroll_types == null || scroll_types.length == 0) {
            scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
            scrollUnits1=SCROLL_TYPE.YEAR.value + SCROLL_TYPE.MONTH.value+ SCROLL_TYPE.DAY.value;
        } else {
            if (scroll_types.length==2){
                for (SCROLL_TYPE scroll_type : scroll_types) {
                    scrollUnits ^= scroll_type.value;
                }
            }
            if(scroll_types.length==3){
                for (SCROLL_TYPE scroll_type : scroll_types) {
                    scrollUnits1 ^= scroll_type.value;
                }
            }

        }
        return scrollUnits;
    }

    public void show(String time) {
        String temp="yyyy-MM-dd HH:mm";

        if (canAccess) {
            if (isValidDate(time, temp)) {
                if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                    canAccess = true;
                    initParameter();
                    initTimer();
                    addListener();
                    setSelectedTime(time);
                    datePickerDialog.show();
                }
            } else {
                canAccess = false;
            }
        }
    }

    /**
     * 设置日期控件是否显示时和分
     */
    public void showSpecificTime(boolean show) {
        if (canAccess) {
            if (show) {
                disScrollUnit();
                hour_pv.setVisibility(View.VISIBLE);
                hour_text.setVisibility(View.VISIBLE);
                minute_pv.setVisibility(View.VISIBLE);
                minute_text.setVisibility(View.VISIBLE);
            } else {
                disScrollUnit(SCROLL_TYPE.HOUR, SCROLL_TYPE.MINUTE);
                hour_pv.setVisibility(View.GONE);
                hour_text.setVisibility(View.GONE);
                minute_pv.setVisibility(View.GONE);
                minute_text.setVisibility(View.GONE);
            }
        }
    }
    /**
     * 设置日期控件是否显示年月日
     */
    public void showSpecificDate(boolean show) {
        if (canAccess) {
            if (show) {
                disScrollUnit();
                year_pv.setVisibility(View.VISIBLE);
                year_text.setVisibility(View.VISIBLE);
                month_pv.setVisibility(View.VISIBLE);
                month_text.setVisibility(View.VISIBLE);
                day_pv.setVisibility(View.VISIBLE);
                day_text.setVisibility(View.VISIBLE);
            } else {
                disScrollUnit(SCROLL_TYPE.YEAR, SCROLL_TYPE.MONTH,SCROLL_TYPE.DAY);
                year_pv.setVisibility(View.GONE);
                year_text.setVisibility(View.GONE);
                month_pv.setVisibility(View.GONE);
                month_text.setVisibility(View.GONE);
                day_pv.setVisibility(View.GONE);
                day_text.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
            this.month_pv.setIsLoop(isLoop);
            this.day_pv.setIsLoop(isLoop);
            this.hour_pv.setIsLoop(isLoop);
            this.minute_pv.setIsLoop(isLoop);
        }
    }

    public void setYearIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
        }
    }

    public void setMonIsLoop(boolean isLoop) {
        if (canAccess) {
            this.month_pv.setIsLoop(isLoop);
        }
    }

    public void setDayIsLoop(boolean isLoop) {
        if (canAccess) {
            this.day_pv.setIsLoop(isLoop);
        }
    }

    public void setHourIsLoop(boolean isLoop) {
        if (canAccess) {
            this.hour_pv.setIsLoop(isLoop);
        }
    }

    public void setMinIsLoop(boolean isLoop) {
        if (canAccess) {
            this.minute_pv.setIsLoop(isLoop);
        }
    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time) {
        if (canAccess) {
            String[] str = time.split(" ");
            if (scrollUnits1==0){
                String[] timeStr = str[1].split(":");
                if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
                    hour.clear();
                    for (int i = startHour; i <= MAX_HOUR; i++) {
                        hour.add(formatTimeUnit(i));
                    }
                    hour_pv.setData(hour);
                    hour_pv.setSelected(timeStr[0]);
                    currentHour = timeStr[0]; //保存选择的小时
                    selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]));
                    executeAnimator(hour_pv);
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
                    minute.clear();
                    int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
                    if (selectedHour == startHour) {
                        for (int i = startMinute; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    } else if (selectedHour == endHour) {
                        for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    } else {
                        for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    }
                    minute_pv.setData(minute);
                    minute_pv.setSelected(timeStr[1]);
                    currentMin = timeStr[1]; //保存选择的分钟
                    selectedCalender.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
                    executeAnimator(minute_pv);

                }
            }else{
                String[] dateStr = str[0].split("-");
                year_pv.setSelected(dateStr[0]);
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));

                month.clear();
                int selectedYear = selectedCalender.get(Calendar.YEAR);
                if (selectedYear == startYear) {
                    for (int i = startMonth; i <= MAX_MONTH; i++) {
                        month.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear) {
                    for (int i = 1; i <= endMonth; i++) {
                        month.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= MAX_MONTH; i++) {
                        month.add(formatTimeUnit(i));
                    }
                }
                month_pv.setData(month);
                month_pv.setSelected(dateStr[1]);
                currentMon = dateStr[1]; //保存选择的月份
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
                executeAnimator(month_pv);

                day.clear();
                int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
                if (selectedYear == startYear && selectedMonth == startMonth) {
                    for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        day.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear && selectedMonth == endMonth) {
                    for (int i = 1; i <= endDay; i++) {
                        day.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        day.add(formatTimeUnit(i));
                    }
                }
                lastMonthDays = day.size();
                day_pv.setData(day);
                day_pv.setSelected(dateStr[2]);
                currentDay = dateStr[2]; //保存选择的日
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
                executeAnimator(day_pv);

                String[] timeStr = str[1].split(":");
                if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
                    hour.clear();
                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                        for (int i = startHour; i <= MAX_HOUR; i++) {
                            hour.add(formatTimeUnit(i));
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                        for (int i = MIN_HOUR; i <= endHour; i++) {
                            hour.add(formatTimeUnit(i));
                        }
                    } else {
                        for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                            hour.add(formatTimeUnit(i));
                        }
                    }
                    hour_pv.setData(hour);
                    hour_pv.setSelected(timeStr[0]);
                    currentHour = timeStr[0]; //保存选择的小时
                    selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]));
                    executeAnimator(hour_pv);
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
                    minute.clear();
                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                    int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
                    if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                        for (int i = startMinute; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                        for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    } else {
                        for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                            minute.add(formatTimeUnit(i));
                        }
                    }
                    minute_pv.setData(minute);
                    minute_pv.setSelected(timeStr[1]);
                    currentMin = timeStr[1]; //保存选择的分钟
                    selectedCalender.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
                    executeAnimator(minute_pv);
                }
            }
            executeScroll();
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
