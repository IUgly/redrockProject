package team.redrock.running.util;

public class Util {
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }
    public static void partPage(String pageParam, int start, int end){
        int page = 1;
        if (pageParam!=null){
            page = Integer.valueOf(pageParam);
        }
        if (page >1){
            start = 15*(page-1);
            end = start+14;
        }
    }
}
