package com.onetoo.www.onetoo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by longShun on 2016/10/16.
 * 时间日期相关的工具类
 * java中获取当前时间的时间戳是13位数的；
 * 注意：如果服务器返回的时间戳是10位数，需要乘以1000在使用
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis timeInMillis
     * @param dateFormat   dateFormat
     * @return String
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis timeInMillis
     * @return String
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return long
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return String
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return String
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     */
    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(timeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据时间戳（13位数，10位数需要乘以1000）获取当前月份
     * @param timeStamp 时间戳
     * @return month
     */
    public static int getMonth(long timeStamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        //Time.MONTH及Calendar.MONTH 默认的月份为  0-11
        return c.get(Calendar.MONTH)+1;
    }

    public static String getWeek(long timeStamp) {
        String week = "周";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                week += "日";
                break;
            case 2:
                week += "一";
                break;
            case 3:
                week += "二";
                break;
            case 4:
                week += "三";
                break;
            case 5:
                week += "四";
                break;
            case 6:
                week += "五";
                break;
            case 7:
                week += "六";
                break;
        }
        return week;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  格式如2012-09-08
     * @return  星期几
     */
    public static String getWeek(String pTime) {
        String week = null;
        try {
            Date date = DATE_FORMAT_DATE.parse(pTime);
            long timeStamp = date.getTime();
            week = getWeek(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }

    public static String convertSecondToDayHourMinSec(long second){
        StringBuilder sb = new StringBuilder();
        int day = (int) (second / 60 / 60 / 24);//天数
        if (day > 0) {//最大天数1
            sb.append(day+"天");
            int hour = (int) (second / 60 / 60 % 24);
            if (hour > 0) {
                sb.append(hour+"小时");
                int min = (int) (second / 60 % 60);
                if (min > 0) {
                    sb.append(min+"分钟");
                }
            }
        } else {
            int hour = (int) (second / 60 / 60);
            if (hour > 0) {//最大小时2
                sb.append(hour+"小时");
                int min = (int) (second % 60);
                if (min > 0) {
                    sb.append(min+"分钟");
                }
            }else {//最大分钟3
                int min = (int) (second / 60);
                if (min > 0) {
                    sb.append(min+"分钟");
                }else {//最大秒4
                    sb.append(second+"秒");
                }
            }
        }
        return sb.toString();
    }
}
