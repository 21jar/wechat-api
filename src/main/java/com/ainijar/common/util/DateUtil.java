package com.ainijar.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author panyiwei
 * @create 2018-10-15 14:56
 */
public class DateUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY = "yyyy";

    public static final String YYYY_MM = "yyyy-MM";

    /**
     * 格式化日期
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date parseDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 获取某月第一天
     * @param dateStr
     * @return
     */
    public static String getMonthFirstday(String dateStr, String format) throws ParseException {
        SimpleDateFormat formater=new SimpleDateFormat(format);
        SimpleDateFormat formater1=new SimpleDateFormat(YYYY_MM);
        Date date = formater1.parse(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //设置为1号,当前日期既为本月第一天 
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = formater.format(c.getTime());
        return first;
    }

    /**
     * 获取某月的下一月第一天
     * @param dateStr
     * @return
     */
    public static String getNextMonthFirstday(String dateStr, String format) throws ParseException {
        SimpleDateFormat formater=new SimpleDateFormat(format);
        SimpleDateFormat formater1=new SimpleDateFormat(YYYY_MM);
        Date date = formater1.parse(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = formater.format(c.getTime());
        return first;
    }


    /**
     * 得到某年本日
     * @return
     */
    public static String getYearToday(int year){
        SimpleDateFormat formater=new SimpleDateFormat(YYYY_MM_DD);
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.YEAR,year);
        Date date=cal.getTime();
        return formater.format(date);
    }

    /**
     * 得到上年本日
     * @return
     */
    public static String getLastYearToday(String format){
        SimpleDateFormat formater=new SimpleDateFormat(format);
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        //设置为1号,当前日期既为本月第一天 
        cal.set(Calendar.DAY_OF_YEAR,-1);
        Date date=cal.getTime();
        return formater.format(date);
    }

    /**
     * 得到本周周一日期
     * @return
     */
    public static String getWeekMonday(String format){
        SimpleDateFormat formater=new SimpleDateFormat(format);
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date first=cal.getTime();
        return formater.format(first);
    }

    /**
     * 得到上周周一日期
     * @return
     */
    public static String getLastWeekMonday(String format){
        SimpleDateFormat formater=new SimpleDateFormat(format);
        // n为推迟的周数，-1上周，0本周，1下周，2下下周，依次类推
        int n1 = -1;
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, n1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date first=cal.getTime();
        return formater.format(first);
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static String getThisMonthFirstday(String format){
        SimpleDateFormat formater=new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天 
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = formater.format(c.getTime());
        return first;
    }

    /**
     * 获取上月月第一天
     * @return
     */
    public static String getThisLastMonthFirstday(String format){
        SimpleDateFormat formater=new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = formater.format(c.getTime());
        return first;
    }

}
