package cn.afj.es2csv.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class DateTimeUtils {
    /**
     * 日期没有分隔符
     */
    public static final String DATE_NO_SEPARATOR = "YYYYMMdd";

    /**
     * 没有分隔符的日期格式化“YYYYMMdd”
     * @param now
     * @return
     */
    public static String dateNoSeparator(LocalDate now){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_NO_SEPARATOR);
        return now.format(formatter);
    }

    /**
     * 获得当前时间，没有分隔符的日期格式化“YYYYMMdd”
     * @return
     */
    public static String dateNoSeparator(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_NO_SEPARATOR);
        return LocalDate.now().format(formatter);
    }

    /**
     * 获得当前时间到1970年的毫秒数
     * @return
     */
    public static Long millisecond(){
        return Clock.systemUTC().millis();
    }



    /**
     * 将本地时间转化成UTC时区时间，并返回前一个小时的UTC时区时间范围
     * @return
     */
    public static TwoTuple<String,String> getUtcDateTimeRange(){
        String bengin = DateTime.now(DateTimeZone.UTC).plusHours(-1).toString("YYYY-MM-dd'T'HH:00:00.000'Z'");
        String end = DateTime.parse(bengin).plusHours(1).minusSeconds(1).toString("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
        return new TwoTuple<>(bengin,end);
    }

    /**
     * 根据传入的时间格式获取前一个小时的utc时间区间
     * @param date 传入的应是一个UTC时间其格式遵循YYYY-MM-dd'T'HH:mm:ss.SSS'Z'
     * @return
     */
    public static TwoTuple<String,String> getUtcDateTimeRange(String date){
        String bengin = DateTime.parse(date, DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC()).plusHours(-1).toString("YYYY-MM-dd'T'HH:00:00.000'Z'");
        String end = DateTime.parse(bengin).plusHours(1).minusSeconds(1).toString("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
        return new TwoTuple<>(bengin,end);
    }


    /**
     * 根据当前时间获取上一个小时时间范围区间
     * @return
     */
    public static TwoTuple<String,String> getLocalDateTimeRangeString(){
        DateTime bengin = DateTime.now(DateTimeZone.forID("Asia/Shanghai")).plusHours(-1);
        String benginStr = bengin.toString("yyyy-MM-dd HH:00:00");
        String endStr = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:00:00")).plusHours(1).minusSeconds(1).toString("yyyy-MM-dd HH:mm:ss");
        return new TwoTuple<>(benginStr,endStr);
    }

    /**
     * 根据当前时间获取上一个小时时间范围区间
     * @return
     */
    public static Pair<DateTime, DateTime> getLocalDateTimeRangeDateTime(){
        DateTime bengin = DateTime.now(DateTimeZone.forID("Asia/Shanghai")).plusHours(-1);
        String benginStr = bengin.toString("yyyy-MM-dd HH:00:00");
        bengin = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:00:00"));
        DateTime end = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:00:00")).plusHours(1);
        return Pair.of(bengin,end);
    }

    /**
     * 根据当前时间获取昨天时间范围区间
     * @return
     */
    public static Pair<DateTime, DateTime> getLocalDateRangeDate(){
        DateTime bengin = DateTime.now(DateTimeZone.forID("Asia/Shanghai")).plusDays(-1);
        String benginStr = bengin.toString("yyyy-MM-dd 00:00:00");
        bengin = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd 00:00:00"));
        DateTime end = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd 00:00:00")).plusDays(1);
        return Pair.of(bengin,end);
    }

    /**
     * 获取给定字符串的0点到24点时间
     * @param str
     * @return
     */
    public static Pair<DateTime, DateTime> getDateTimeRangeBYString(String str){
        DateTime dateTime = DateTime.parse(str, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime beginDateTime = DateTime.parse(dateTime.toString("yyyy-MM-dd 00:00:00"), DateTimeFormat.forPattern("yyyy-MM-dd 00:00:00"));
        DateTime endDateTime = beginDateTime.plusHours(24);
        return Pair.of(beginDateTime,endDateTime);
    }


    /**
     * 根据指定的时间获取前一个小时时间范围区间
     * @param date
     * @return
     */
    public static TwoTuple<String,String> getLocalDateTimeRange(String date){
        DateTime bengin = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.forID("Asia/Shanghai"))).plusHours(-1);
        String benginStr = bengin.toString("yyyy-MM-dd HH:00:00");
        String endStr = DateTime.parse(benginStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:00:00")).plusHours(1).minusSeconds(1).toString("yyyy-MM-dd HH:mm:ss");
        return new TwoTuple<>(benginStr,endStr);
    }


    public static String formatDateTime(String date){
        try {
            return DateTime.parse(date, DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'+0800'")).toString("YYYY-MM-dd HH:mm:ss");
        } catch (Exception e) {
            return DateTime.parse(date, DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss'+0800'")).toString("YYYY-MM-dd HH:mm:ss");
        } finally {
        }
    }

    public static Long parseDateTime(String date){
        return DateTime.parse(date, DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")).toDate().getTime();
    }

    /**
     * 毫秒转化时分秒毫秒
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

}
