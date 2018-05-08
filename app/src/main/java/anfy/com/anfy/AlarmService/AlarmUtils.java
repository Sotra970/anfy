package anfy.com.anfy.AlarmService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.Activity.SplashActivity;
import anfy.com.anfy.R;


/**
 * Created by developers@appgain.io on 4/26/2018.
 */

public class AlarmUtils {
    public  static  final int falgs   = PendingIntent.FLAG_UPDATE_CURRENT  ;
    public  static  final long INTERVAL_HOUR = 60 * 60 *1000 ;
    public  static  final long INTERVAL_DAY = INTERVAL_HOUR*24 ;
    public  static  final long INTERVAL_WEEK = INTERVAL_DAY*7 ;
    private static final String TAG = "AlarmUtils";

    public  static  void showNotification(Context context  , Bundle bundle){
        if (bundle!=null){
            releaseNotification(context , bundle.getString("message"));
        }else {
            Log.e("AlarmUtils" , "showNotification "+ " bundle==null") ;
        }
    }

//    adb shell
    //adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.quantatil.ikhair.app
// am broadcast -a android.intent.action.BOOT_COMPLETED -c android.intent.category.HOME -n com.quantatil.ikhair.app/SampleBootReceiver

    public  static  void setAlarm(Context context, int requestCode, long trigger_time , long interval , Bundle bundle){
        bundle.putInt("requestCode" , requestCode);

        // preparing
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context ,  AlarmServiceReciver.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , requestCode , intent , PendingIntent.FLAG_UPDATE_CURRENT    ) ;
        // setting alarm
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP , trigger_time , interval , pendingIntent);


        Log.e("AlarmUtils" , "setAlarm to :"+ TimeUtils.getFullDate(trigger_time) +"  for every" + TimeUnit.MILLISECONDS.toMinutes(interval) +"//interval");
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
    }

    public  static  void cancelAlarm(Context context , int reqestCode ){
        Log.e(TAG  , "cancelAlarm foro :" + reqestCode);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context ,  AlarmServiceReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , reqestCode , intent , falgs) ;
        alarmManager.cancel(pendingIntent);

    }

    static void releaseNotification(Context context, String message){
        Intent intent = new Intent( context , SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.logo_no_bg)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                ;
        final Notification notification = notificationBuilder.build();
        long id =  Calendar.getInstance().getTimeInMillis() ;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) id,notification);


    }



}
