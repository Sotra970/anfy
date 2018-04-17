package anfy.com.anfy.Util;

import android.content.Context;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.R;

public class TimeUtils {

    public static String getFromWhen(long timeStamp, Context context){
        timeStamp = TimeUnit.SECONDS.toMillis(timeStamp);
        long now = System.currentTimeMillis();
        long diff = now - timeStamp;
        long min , hours , day , month , year ;
        min = TimeUnit.MILLISECONDS.toMinutes(diff);
        hours = TimeUnit.MILLISECONDS.toHours(diff);
        day = TimeUnit.MILLISECONDS.toDays(diff);
        month = day / 30;
        year = month / 12;
        String from = "" ;
        if (year >= 1){
            from = year + " " + context.getString(R.string.years);
        }else if(month >= 1){
            from = month + " " + context.getString(R.string.months);
        } else if (day >= 1) {
            from = day +   " " + context.getString(R.string.days);
        }else if (hours >= 1) {
            from = hours +   " " + context.getString(R.string.hours);
        }else if (min >= 1) {
            from = min +   " " + context.getString(R.string.mins);
        }else{
            from = context.getString(R.string.now);
        }
        return from;
    }
}
