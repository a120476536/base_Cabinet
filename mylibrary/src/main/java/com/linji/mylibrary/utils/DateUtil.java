package com.linji.mylibrary.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    //获得当前年月日时分秒星期

    public static String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : "" + c.get(Calendar.MINUTE);//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒

        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "/" + mMonth + "/" + mDay + "  " + "星期" + mWay + "  " + mHour + ":" + mMinute;
    }

    public static String getWeek() {
        final Calendar c = Calendar.getInstance();
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }


    /**
     * 当前时间是否在规则时间段内
     * @param time
     * @return
     */
    public static boolean during( String time) {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        boolean fit = false;
        String[] times = time.split("-");
        String startTime = times[0];
        String endTime = times[1];
        String[] startTimes = startTime.split(":");
        String[] endTimes = endTime.split(":");
        int startHour = Integer.parseInt(startTimes[0]);
        int startMinute = Integer.parseInt(startTimes[1]);
        int endHour = Integer.parseInt(endTimes[0]);
        int endMinute = Integer.parseInt(endTimes[1]);
        int current = currentHour * 60 + currentMinute;
        int start = startHour * 60 + startMinute;
        int end = endHour * 60 + endMinute;
        if (start <= current && current <= end) {
            fit = true;
        }
        return fit;
    }

    public static String getCurrentDate(String type) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( type, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static boolean isZero() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
        if (mHour.equals("0") && mMinute.equals("0") && mSecond.equals("0")) {
            return true;
        }
        return false;
    }
}
