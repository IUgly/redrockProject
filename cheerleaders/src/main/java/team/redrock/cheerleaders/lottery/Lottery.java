package team.redrock.cheerleaders.lottery;


import com.google.gson.Gson;

import java.util.Arrays;


public class Lottery {
    //使用并行化数组操作 初始化数组
    public static double[] parallelInitialize(int size){
        double[] values = new double[size];
        Arrays.parallelSetAll(values, i -> i);
        return values;
    }

    public static void main(String[] args) {
        System.out.println(new Gson().toJson(parallelInitialize(10)));
    }
}
