package com.helloscala.common.utils;

import com.helloscala.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DateUtil {
    public static final String STARTTIME = " 00:00:00";
    public static final String ENDTIME = " 23:59:59";

    public final static String FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public final static String[] REPLACE_STRING = new String[]{"GMT+0800", "GMT+08:00"};
    public final static String SPLIT_STRING = "(中国标准时间)";
    public static Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String MM_DD = "MM-dd";
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYYMMDD = "yyyyMMdd";
    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private DateUtil() {
    }

    public static String getNowTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static Date str2Date(String dateString) {
        try {
            dateString = dateString.split(Pattern.quote(SPLIT_STRING))[0].replace(REPLACE_STRING[0], REPLACE_STRING[1]);
            SimpleDateFormat sf1 = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
            return sf1.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException("Date format error, " + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + FORMAT_STRING + "]");
        }
    }

    public static String getToDayStartTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    public static String getToDayEndTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date date = new Date(System.currentTimeMillis());
        return formate.format(date);
    }

    public static String getYestodayStartTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        return formate.format(date);
    }

    public static String getYestodayEndTime() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        return formate.format(date);
    }

    public static String getOneDayStartTime(String oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(oneDay);
        return formate.format(date);
    }

    public static String getOneDayStartTime(Date oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(oneDay);
    }

    public static String getOneDayEndTime(String oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(oneDay);
        return formate.format(date);
    }

    public static String getOneDayEndTime(Date oneDay) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(oneDay);
    }


    public static Date getWeekStartTime() {
        // 获得本周一0点时间
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    public static Date strToDateTime(String dateTime) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateTime);
        } catch (ParseException e) {
            throw new BusinessException("Date time deserialize failed!", e);
        }
        return date;
    }

    public static Date strToDateTime(String dateTime,String formatStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            date = format.parse(dateTime);
        } catch (ParseException e) {
            throw new BusinessException("Date time deserialize error!", e);
        }
        return date;
    }

    public static Long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }

    public static String dateTimeToStr(Date dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(dateTime);
    }

    public static String dateTimeToStr(Date dateTime,String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(dateTime);
    }

    public static String getWeekStartTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(cal.getTime());
    }

    public static Date getWeekEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    public static String getWeekEndTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartTime());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    public static String getLastWeekStartTimeStr() {
        int weeks = -1;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return formate.format(monday);
    }

    public static Date getMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static String getMonthStartTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    public static Date getMonthEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    public static String getMonthEndTimeStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return formate.format(cal.getTime());
    }

    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static int getDayByTwoDay(String date1, String date2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0L;
        try {
            Date date = myFormatter.parse(date1);
            Date mydate = myFormatter.parse(date2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 0;
        }
        return (int) day;
    }

    public static int getSecondByTwoDay(Date lastDate, Date date) {
        long second = 0L;
        try {
            second = (lastDate.getTime() - date.getTime()) / 1000;
        } catch (Exception e) {
            return 0;
        }
        return (int) second;
    }

    public static int getDaysByWeek(String dateTime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateTime);
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        day = day - 1;
        if (day == 0) {
            day = 7;
        }
        return day;
    }

    public static int getDaysByMonth(String dateTime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateTime);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }


    public static Integer getYears() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.YEAR);

    }

    public static Integer getMonth() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.MONTH) + 1;

    }

    public static Integer getDay() {
        Calendar calendar = new GregorianCalendar(TimeZone
                .getDefault());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressWarnings("unused")
    public static String getTime(double hour) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        long time = (long) (System.currentTimeMillis() + hour * 60 * 60 * 1000L);
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
        return formate.format(date);
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        // 因为按中国礼拜一作为第一天所以这里减1
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    public static Date getDate(String date, int day) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date beforeDate = formate.parse(date);
            cal.setTime(beforeDate);
            cal.add(Calendar.DAY_OF_MONTH, day);
            //cal.set(beforeDate.getYear(), beforeDate.getMonth(), beforeDate.getDay()+day, beforeDate.getHours(),beforeDate.getSeconds(), beforeDate.getMinutes());
            return cal.getTime();
        } catch (ParseException e) {
            log.error("Failed to ge date from str={}, day={}", date, day, e);
        }
        return null;
    }

    public static String getDateStr(Date beforeDate, Long timeSecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Long time = beforeDate.getTime() + timeSecond * 1000;
            return format.format(time);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    public static String formateDate(Date date, String code) {
        SimpleDateFormat formate = new SimpleDateFormat(code);
        return formate.format(date);

    }

    public static ArrayList<String> getDaysByN(int intervals, String formatStr) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        for (int i = intervals - 1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i, formatStr));
        }
        return pastDaysList;
    }

    public static String getPastDate(int past, String formatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(today);
    }

    public static List<String> getDayBetweenDates(String begin, String end) {
        Date dBegin = strToDateTime(begin);
        Date dEnd = strToDateTime(end);
        List<String> lDate = new ArrayList<>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static long getDiffDateToMinutes (Date endDate, Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        long startTimeMillis = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long endTimeMillis = calendar.getTimeInMillis();
        long durationMillis = startTimeMillis - endTimeMillis;
        return durationMillis / (60 * 1000);
    }
}
