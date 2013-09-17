package com.leansoftwarelabs.ext.jsf;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

public final class UtilFunctions {
    private UtilFunctions() {
    }
    public static String formatDate(Date date, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    public static Date dateAdd(Date date, int interval, int value){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(interval, cal.get(interval)+value);
        return cal.getTime();
    }
    public static void main(String[] args) {
        System.out.println("YEAR: " + Calendar.YEAR);
       System.out.println("MONTH: "+ Calendar.MONTH);
       System.out.println("DAY_OF_MONTH:" + Calendar.DAY_OF_MONTH);
       System.out.println("DAY_OF_YEAR:" + Calendar.DAY_OF_YEAR);
       System.out.println("test...");
       Calendar cal = Calendar.getInstance();
       System.out.println(cal.get(Calendar.DAY_OF_YEAR));
       System.out.println(cal.get(Calendar.DAY_OF_MONTH));
   }
}
