package com.seki.noteasklite.Util;

import android.text.TextUtils;


import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuan on 2015/7/26.
 */
public class TimeLogic {
    private static String DATE_FORMAT =  "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CH = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_CURRENT_YEAR = new SimpleDateFormat(
            "MM-dd");
    /**
     * 日期字符串转换为Date
     * @param dateStr
     * @param format
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;
        try {
        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateStr);
        }
            else
        {
            dateStr = "1970-1-1 24:59:59";
            DateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateStr);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 日期转换为字符串
     * @param timeStr
     * @param format
     * @return
     */
    public static String dateToString(String timeStr, String format) {
        // 判断是否是今年
        Date date = strToDate(timeStr, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 如果是今年的话，才去“xx月xx日”日期格式
        if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            return DATE_FORMAT_TILL_DAY_CURRENT_YEAR.format(date);
        }

        return DATE_FORMAT_TILL_DAY_CH.format(date);
    }
    /**
     * 日期逻辑
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(dateStr, DATE_FORMAT);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1小时内
            return sb.append(time + MyApp.getInstance().getApplicationContext().getResources().getString(R.string.time_second)).toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60+ MyApp.getInstance().getApplicationContext().getResources().getString(R.string.time_minite)).toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + MyApp.getInstance().getApplicationContext().getResources().getString(R.string.time_hour)).toString();
        }else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append(MyApp.getInstance().getApplicationContext().getResources().getString(R.string.yesteday)).toString();
        }else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append(MyApp.getInstance().getApplicationContext().getResources().getString(R.string.after_tomarrow)).toString();
        }else if (time >= 3600 * 72) {
            return dateToString(dateStr, DATE_FORMAT);
        }
        return dateToString(dateStr, DATE_FORMAT);
    }
    public static String getNowTimeFormatly(String format){
        return  new SimpleDateFormat(format).format( new Date());
    }
    public static String getNowTimeFormatly(){
        return  getNowTimeFormatly("yyyy-MM-dd HH-mm-ss");
    }
    public static String getNowDateFormatly(){
        return  getNowTimeFormatly("yyyy-MM-dd");
    }
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat ssf = new SimpleDateFormat("yyyy-MM-dd");
        return ssf.format(d);
       }
    public static String getTimeToFormatString(long time,String format) {
        Date d = new Date(time);
        SimpleDateFormat ssf = new SimpleDateFormat(format);
        return ssf.format(d);
    }
}
