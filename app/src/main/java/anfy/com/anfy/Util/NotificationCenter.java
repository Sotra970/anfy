package anfy.com.anfy.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmed on 10/16/2017.
 */

public class NotificationCenter {

    private final static String PREFERENCE_FILE_SEEN_NOTIFICATIONS =
            "PREFERENCE_FILE_SEEN_NOTIFICATIONS";

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences initSharedPreferences(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(
                    PREFERENCE_FILE_SEEN_NOTIFICATIONS,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void setNotificationSeen(String notificationId, Context context){
        if(sharedPreferences == null){
            initSharedPreferences(context);
        }

        if(sharedPreferences != null){
            sharedPreferences.edit().putBoolean(notificationId, true).apply();
        }
    }

    public static boolean isNotificationSeen(String notificationId, Context context){
        if(sharedPreferences == null){
            initSharedPreferences(context);
        }

        if(sharedPreferences != null){
            return sharedPreferences.getBoolean(notificationId, false);
        }

        return false;
    }
}
