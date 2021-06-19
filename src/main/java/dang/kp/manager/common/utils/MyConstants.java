package dang.kp.manager.common.utils;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyConstants {
    public static final SimpleDateFormat SDF_FULL = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
    public static final SimpleDateFormat SDF_SIMPLE = new SimpleDateFormat("yyyyMMdd");

    // 补位字符
    public static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final String[] SYSTEM_FIELDS = {"id", "createTime", "updateTime", "delFlag"};
    public static final String MY_CRON = "myCron";
    public static final String MY_CRON_VAL = "0 0 0/1 * * ?";

    public static final List<String> CFG_PATH = Lists.newArrayList("/opt/logs", "/opt/apps");
    public static final List<String> CFG_EXT = Lists.newArrayList(".log", ".gz", ".zip", "-running.jar.20", "log.20", "app-20");

    public enum MyKey {
        BaseAdminUser("01"),
        BaseAdminPermission("02"),
        BaseAdminRole("03"),
        DictType("04"),
        DictItem("05"),
        BatchConfig("10"),
        FilePathConfig("11"),
        BatchLog("12"),
        BatchLogNo("13");
        private final String value;

        MyKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
