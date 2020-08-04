package com.example.ekhuibaselibrary.utils;


import com.example.ekhuibaselibrary.R;

import java.lang.reflect.Field;

/**
 * Created by Ekhui on 2020/7/4.
 */
public class FiledUtils {

    public static int getResId(String name) {
        Field field;
        try {
            field = R.drawable.class.getField(name);
            return Integer.parseInt(field.get(null).toString());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
