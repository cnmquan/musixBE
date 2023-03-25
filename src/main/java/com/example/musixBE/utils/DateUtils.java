package com.example.musixBE.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String toString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(date);
    }

    public static String toCurrentDateString() {
        return DateUtils.toString(new Date());
    }
}
