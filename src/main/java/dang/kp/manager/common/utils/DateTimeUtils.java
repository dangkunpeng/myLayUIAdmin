package dang.kp.manager.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateTimeUtils {
    // 1天的秒数 1000 * 3600 * 24
    private static final Long DAY_IN_MILLIS = 86400000L;

    public static String getDay() {
        return MyConstants.SDF_YYYYMMDD.format(new Date());
    }

    public static String getHMSDay() {
        return MyConstants.SDF_YMD_HMS.format(new Date());
    }

    public static String getDayFull(Long time) {
        return MyConstants.SDF_FULL.format(new Date(time));
    }

    /**
     * 生成主键
     *
     * @return
     */
    public static Long getLastPositiveDate(Integer overDueDays) {
        return lastSomeDay(overDueDays).getTime();
    }

    public static String getSomeDay(Integer days) {
        return MyConstants.SDF_SIMPLE.format(lastSomeDay(days));
    }

    public static Date lastSomeDay(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0 - days);
        return calendar.getTime();
    }

    public static String getPastDateTime(Integer days) {
        return MyConstants.SDF_SIMPLE.format(new Date(System.currentTimeMillis() - days * DAY_IN_MILLIS)) + "000000";
    }
}
