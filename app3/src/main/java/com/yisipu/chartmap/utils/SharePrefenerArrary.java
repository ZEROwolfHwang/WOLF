package com.yisipu.chartmap.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/11/14.
 */
public class SharePrefenerArrary {
    public static String[] getSharedPreference(Context mContext,String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);

        return str;
    }

    public static void setSharedPreference(Context mContext,String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }



}
