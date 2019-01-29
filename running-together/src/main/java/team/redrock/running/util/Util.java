package team.redrock.running.util;

public class Util {
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "[0-9]*";//是数字返true
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

    public static void main(String[] args) {
        System.out.println(isLetterDigitOrChinese("2017211903"));
    }
}
