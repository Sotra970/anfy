package anfy.com.anfy.AlarmService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by developers@appgain.io on 4/23/2018.
 */

public class TimeUtils {

    public  static  final String DAY_MONTH_FORMAT = "dd-MMMM";
    public static String getDayMonthString(long time) {
        SimpleDateFormat format  = new SimpleDateFormat(DAY_MONTH_FORMAT , new Locale("ar")) ;
        String s = format.format(new Date(time));
        return s;
    }

    public  static  final String HOUTS_MONTH_FORMAT = "hh:mm a";
    public static String getHours(long timeInMillis) {
        SimpleDateFormat format  = new SimpleDateFormat(HOUTS_MONTH_FORMAT , new Locale("en")) ;
        String s = format.format(new Date(timeInMillis));
        return s;
    }

    public static String getFullDate(long timeInMillis) {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd h:m:s a" , new Locale("en")) ;
        String s = format.format(new Date(timeInMillis));
        return s;
    }
}
