package com.example.helloworld;

import android.util.Log;

public class Utils {
    public static String[] getBackgroundColors(int num){
        String[] result = new String[num];
        //背景颜色
        for(int i=0;i<num;i++){
            String colorString = "#30";
            for(int j=0;j<6;j++ ){
                int co =(int) (10*Math.random());
                if(co==10){
                    co-=1;
                }
                colorString += co;//#FFxxxx
            }
            Log.i(Constant.LOG_KEY,"color:"+colorString);
            result[i] = colorString;
        }
        return result;
    }
}
