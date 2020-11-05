package cn.afj.es2csv.util;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @Author afj
 * @Date 2020/9/2 11:42
 * @Version 1.0
 * @description:
 */
public class DateUtils {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final String  DATETIME_PATTERN_NO_MSE = "yyyy-MM-dd HH:mm";

    public static final String DATETIME_PATTERN_DATE = "yyyy-MM-dd";

    public static final String DATETIME_PATTERN_MONTH = "yyyy年MM月dd日HH点mm分";

    public static final String DATETIME_PATTERN_DATE_NO_ = "yyyyMMdd";

    public static final String DATE_FULL_STR_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    public static final String DATE_FULL_SS_NO_SP = "yyyyMMddHHmmss";

    public static final String DATETIME_NO_HOUR = "yyyyMMdd";

    public static final String DATETIME_YEAR = "yyyy";

    public static final String DATETIME_MONTH = "MM";


    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 秒转换为指定格式的日期
     * @param second
     * @param patten
     * @return
     */
    public static LocalDateTime secondToDate(long second, String patten) {
        Calendar calendar = Calendar.getInstance();
        //转换为毫秒
        calendar.setTimeInMillis(second * 1000);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateString = format.format(date);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, df);
        return dateTime;
    }

    /**
     * @desc 时间字符串转LocalDateTime
     * @param dateStr
     * @param patten
     * @return
     */
    public static LocalDateTime parseDateStrToLocalDateTime(String dateStr, String patten) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, df);
        return dateTime;
    }

    /**
     * @return
     * @Author yaoyang
     * @Description 将时间戳(单位 ： 秒)格式化为指定格式
     * @Date 16:42 2018/12/19
     * @Param
     **/
    public static String timestampFormat(Long timestamp, String format) {
        // 初始化日期类
        Date date = new Date();
        // 设置时间
        date.setTime(timestamp);
        // 设置日期格式
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * @param dateStr 当前时间
     * @param days    时间区间 例如 7 14 30天  必传项
     * @return
     * @desc 根据当前时间和天数  获取周区间
     */
    public static LinkedList<String> getWxDateArea(String dateStr, Integer days) {
        if (days ==null || days == 0) {
            return new LinkedList<String>();
        }
        if (StringUtils.isNotBlank(dateStr)) {
            dateStr = getDateTime(getSpecifiedCountDayBefore(new Date(), 1), DATETIME_PATTERN_DATE);
            System.out.println("上传的时间为空，获取的前一天时间为:" + dateStr);
        }
        //将前一天时间转为时间格式  yyyy-MM-dd
        Date yesterDayDate = parseStr(dateStr, DATETIME_PATTERN_DATE);
        LinkedList<String> resultLinkList = new LinkedList<>();
        String mondays = "";
        Date dateStart = getSpecifiedCountDayBefore(yesterDayDate, days - 1);
        System.out.println("获取的最前时间为:" + getDateTime(dateStart, DATETIME_PATTERN_DATE));
        for (int i = 0; i < days; i++) {
            if (i == 0) {
                mondays = getCurrentWeekFirstDate(yesterDayDate.getTime());
                resultLinkList.addFirst(mondays);
                mondays = "";
            } else {
                mondays = getCurrentWeekFirstDate(getSpecifiedCountDayBefore(yesterDayDate, i).getTime());
                if (!resultLinkList.getFirst().equals(mondays)) {
                    if (parseStr(mondays, DATETIME_PATTERN_DATE).getTime() < dateStart.getTime()) {
                        if (!resultLinkList.getFirst().equals(getDateTime(dateStart, DATETIME_PATTERN_DATE))) {
                            resultLinkList.addFirst(getDateTime(dateStart, DATETIME_PATTERN_DATE));
                        }
                    } else {
                        resultLinkList.addFirst(mondays);
                    }
                }
                mondays = "";
            }
        }
        if (resultLinkList.size() > 0) {
            if (!resultLinkList.getFirst().equals(parseStr(resultLinkList.getFirst(), DATETIME_PATTERN_DATE).getTime())) {
                resultLinkList.removeFirst();
            }
        }
        return resultLinkList;
    }

    public static Date getSpecifiedCountMouthBefore(Date date, Integer count) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, month - count);
        return c.getTime();
    }

    public static Date getSpecifiedCountDayBefore(Date date, Integer count) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - count);
        return c.getTime();
    }

    public static String getTodayCountDayBefore(Integer count) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - count);
        return new SimpleDateFormat(YYYY_MM_DD).format(c.getTime());
    }

    /**
     * 字符时间转date时间
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date parseStr(String dateString, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date parse = simpleDateFormat.parse(dateString);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 日期转为字符串
     *
     * @param value
     * @return
     */
    public static String doConvertToString(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        String result = null;
        if (value instanceof Date) {
            result = simpleDateFormat.format(value);
        }
        return result;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getDateTime(Date time, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(time);
    }

    /**
     * 比较是否滞后于系统时间
     *
     * @param value
     * @return
     */
    public static boolean afterToSystemDate(Object value) {
        boolean result = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN_NO_MSE);
        Date date = null;
        try {
            date = simpleDateFormat.parse((String) value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = date.after(new Date());
        return result;
    }

    public static boolean afterToSysDateInterval(String value, int minutes) {
        Date date = null;
        date = new Date(Long.valueOf(value));
        Date sysDate = new Date();
        // 除以1000是为了转换成秒
        long between = (sysDate.getTime() - date.getTime()) / 1000;
        return between > minutes;
    }

    /**
     * 获取当前时间的毫秒数
     *
     * @return
     */
    public static String getCurrentTimeMillis() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取当前日期的格式化后的字符串
     *
     * @param formart 格式化字符串
     * @return 日期字符串
     *
     * <pre>
     * <li>Author: jackwong</li>
     * <li>Date: 2017年10月12日</li>
     *         </pre>
     */
    public static String getCurrentDateStr(String formart) {
        return getDateTime(new Date(), formart);
    }

    /**
     * 当天的时间戳
     *
     * @param formart
     * @return
     */
    public static Long getCurrentDateLong(String formart) {
        return parseStr(getCurrentDateStr(formart), formart).getTime();
    }


    /**
     * 获得指定日期的前一天
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(Date date) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return c.getTime();
    }

    /**
     * 获得指定日期的前一天
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayAfter(Date date, String patten) {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return DateUtils.getDateTime(c.getTime(), patten);
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2)
            throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(Date str1, Date str2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long time1 = str1.getTime();
        long time2 = str2.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        // day = diff / (24 * 60 * 60 * 1000);
        // hour = (diff / (60 * 60 * 1000) - day * 24);
        // min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return sec + "";
    }

    /**
     * 获取指定日某天所在周的第一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime 指定某天.getTime();
     * @return
     */
    public static String getCurrentWeekFirstDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 0);
        Date result = new Date(calendar.getTimeInMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(result);
    }

    /**
     * 获取指定日某天所在周的最后一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime 指定某天.getTime();
     * @return
     */
    public static String getCurrentWeekLastDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        Date result = new Date(calendar.getTimeInMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(result);
    }

    /**
     * 获取指定日某天所在月的第一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime
     * @return
     */
    public static String getCurrentMonthFirstDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date result = new Date(calendar.getTimeInMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(result);
    }

    /**
     * 获取指定日某天所在月的最后一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime
     * @return
     */
    public static String getCurrentMonthLastDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date result = new Date(calendar.getTimeInMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(result);
    }

    /**
     * 获取指定日某天所在年的第一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime
     * @return
     */
    public static String getCurrentYearFirstDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year + "-01-01";
    }

    /**
     * 获取指定日某天所在年的最后一天 <一句话功能简述> <功能详细描述>
     *
     * @param longTime
     * @return
     */
    public static String getCurrentYearLastDate(Long longTime) {
        Date date = null;
        if (longTime != null) {
            date = new Date(longTime);
        } else {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year + "-12-31";
    }

    /**
     * 通过放入的日期列表获取列表中的最大日期和最小日期
     *
     * @param putInDateList "2016-9-2", "2016-9-1", "2016-8-23", "2016-10-23"
     * @return map.put(" startDate ", " 2016 - 8 - 23 "); map.put("endDate", "2016-10-23");
     */
    public static Map<String, String> getMaxDateAndMinDate(String putInDateList) {
        Map<String, String> dateMap = new HashMap<String, String>(1024);
        if (StringUtils.isEmpty(putInDateList)) {
            return null;
        } else {
            String[] allDates = putInDateList.split(",");
            String startDate = allDates[0];
            String endDate = allDates[0];
            for (int i = 1; i < allDates.length; i++) {
                if (compareDate(startDate, allDates[i])) {
                    startDate = allDates[i];
                }
                if (!compareDate(endDate, allDates[i])) {
                    endDate = allDates[i];
                }
            }
            dateMap.put("startDate", startDate);
            dateMap.put("endDate", endDate);
            return dateMap;
        }
    }

    /**
     * 比较两个日期字符串的大小
     *
     * @param source
     * @param target
     * @return source大于目录，返回true,否则返回false
     */
    public static boolean compareDate(String source, String target) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN_DATE);
            Date sourceDate = simpleDateFormat.parse(source);
            Date targetDate = simpleDateFormat.parse(target);
            if (sourceDate.getTime() > targetDate.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查传入的时间+miuntes分钟后 与当前的时间 相减， 是否在time分钟之内
     *
     * @param requestDate 传入的时间 yyyy-MM-dd HH:mm:ss
     * @param minutes     时间长度
     * @param time        超过时间
     * @return
     */
    public static boolean checkRequestDateIsOvertime(String requestDate, String minutes, String time) {
        SimpleDateFormat sf = new SimpleDateFormat(DATETIME_PATTERN);
        try {
            long s = sf.parse(requestDate).getTime();
            s += Integer.parseInt(minutes) * 60 * 1000;
            s += Integer.parseInt(time) * 60 * 1000;
            if (s <= System.currentTimeMillis()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 当前时间+分钟
     *
     * @param min 要添加的分钟
     */
    public static long addOrMinus(int min)
            throws Exception {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, minute + min);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 当前时间+分钟
     *
     * @param min 要添加的分钟
     */
    public static Date addMinus(int min) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, minute + min);
        return calendar.getTime();
    }

    /**
     * 时间戳转日期  时间戳精确到毫秒
     *
     * @param ccTime
     * @return
     */
    public static String getStrTime(String ccTime) {
        String reStrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        long lccTime = Long.valueOf(ccTime);
        reStrTime = sdf.format(new Date(lccTime));
        return reStrTime;
    }

    /**
     * 时间戳转日期字符串  时间戳精确到秒
     *
     * @param seTime
     * @return 日期字符串
     */
    public static String stamp2StrTime(String seTime) {
        String secondStrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        long secondTime = Long.valueOf(seTime);
        secondStrTime = sdf.format(new Date(secondTime * 1000));
        return secondStrTime;
    }

    /**
     * 根据localDateTime 获取对应的字符串时间
     * 获取失败后默认返回当前时间的字符串时间
     *
     * @return
     */
    public static String getDataStrByLocalDateTime(LocalDateTime localDateTime) {
        try {
            return new SimpleDateFormat(DateUtils.DATETIME_PATTERN_DATE).format(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleDateFormat(DateUtils.DATETIME_PATTERN_DATE).format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        }
    }

    public static String getDateStrByLocalDateTime(LocalDateTime localDateTime) {
        try {
            return new SimpleDateFormat(DateUtils.DATETIME_PATTERN).format(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleDateFormat(DateUtils.DATETIME_PATTERN).format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        }
    }

    /**
     * 获取前几日的时间数据
     *
     * @param howDays
     * @return
     */
    public static List<String> getBeforeDateStrByHowDays(Integer howDays) {
        List<String> beforeDateList = new ArrayList<String>();
        //从当日开始
        int i = 1;
        while (i <= howDays) {
            beforeDateList.add(DateUtils.getDataStrByLocalDateTime(LocalDateTime.now().plusDays(-i)));
            i++;
        }
        listSort(beforeDateList);
        return beforeDateList;
    }

    /**
     * 时间倒叙排列
     *
     * @param list
     */
    private static void listSort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(o1);
                    Date dt2 = format.parse(o2);
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前天的起始时间
     * @return
     */
    public static Date getStartTime() {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day.getTime();
    }

    /**
     * 获取当前天的结束时间
     * @return
     */
    public static Date getEndTime() {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        day.set(Calendar.MILLISECOND, 999);
        return day.getTime();
    }

    /**
     * @desc 昨天开始时间
     * @return
     */
    public static Date getYesterdayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }
    /**
     * @desc 昨天的结束时间
     * @return
     */
    public static Date getYesterdayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 功能：获取本周的开始时间 示例：2013-05-13 00:00:00
     */
    public static long startOfThisWeek() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date=currentDate.getTime();
        return date.getTime();
    }
    /**
     * 功能：获取本周的结束时间 示例：2013-05-19 23:59:59
     */
    public static long endOfThisWeek() {// 当周结束时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.MILLISECOND, 999);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date date=currentDate.getTime();
        return date.getTime();
    }
    /**
     * 功能：获取本月的开始时间
     */
    public static Date startOfThisMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        Date date=currentDate.getTime();
        return date;
    }
    public static Date endOfThisMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date date=cal.getTime();
        return date;
    }
    /**
     * 功能：获取上月的开始时间
     */
    public static Date startOfLastMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.add(Calendar.MONTH, -1);
        Date date=currentDate.getTime();
        return date;
    }
    /**
     * 功能：获取上月的结束时间
     */
    public static long endOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DATE, -1);
        Date date=cal.getTime();
        return date.getTime();
    }

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, DATE_FULL_SS_NO_SP);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN, Locale.CHINA);
        Date usDate = simpleDateFormat.parse(date);
        return DateUtils.getDateFormat(usDate, format);
    }

    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

}
