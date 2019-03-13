package team.redrock.running.util;

public class Util {
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "[0-9]*";//是数字返true
        return str.matches(regex);
    }

}
