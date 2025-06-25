package redcoder.texteditor.utils;

public class SystemUtils {

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }
}
