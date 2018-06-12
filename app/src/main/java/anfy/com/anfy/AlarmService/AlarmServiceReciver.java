package anfy.com.anfy.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;


import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.AnfyDao;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.App.AppController;

/**
 * Created by developers@appgain.io on 4/26/2018.
 */

public class AlarmServiceReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alrmservice" , "" +"onReceive");
        final Bundle extras = intent.getExtras();
        updaateAlarm(context,extras);
    }

    private void showNotification(Context context , Bundle extras) {
        if (extras!=null){
            AlarmUtils.showNotification(context ,extras);
        }
    }

    private void wakeLook(Context context , Bundle extras) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                extras.getInt("requestCode")+"");
        // acquire the lock
        wl.acquire();
        wl.release();
    }

    private void updaateAlarm(Context context , Bundle extras) {
        AppController.getExecutorService().submit(() -> {
            AnfyDao dao = DbApi.dao(context) ;
            List<AlarmEntity> alarmEntities = dao.getReminder(extras.getInt("requestCode"));

            if (alarmEntities !=null &&!alarmEntities.isEmpty()){
                AlarmEntity alarmEntity= alarmEntities.get(0);
                Log.e("Alrmservice" , "" +alarmEntity.toString());
                alarmEntity.starting_time = alarmEntity.interval + alarmEntity.starting_time ;
                if (alarmEntity.isContinous == false){

                    long time = Calendar.getInstance().getTimeInMillis();
                    long start_dif = TimeUnit.MILLISECONDS.toDays(time)  -  TimeUnit.MILLISECONDS.toDays(alarmEntity.starting_date) ;
                    long end_diff =  TimeUnit.MILLISECONDS.toDays(time) - TimeUnit.MILLISECONDS.toDays(alarmEntity.end_data)  ;
                    int days = (int) ((alarmEntity.days_count*7) - start_dif);

                    String TAG = alarmEntity.uiModelName + alarmEntity.take_number;
                    Log.e(TAG , "start : " +  TimeUtils.getFullDate(alarmEntity.starting_date)  +"today " +  TimeUtils.getFullDate(time));
                    Log.e(TAG, "end : " +  TimeUnit.MILLISECONDS.toDays(alarmEntity.end_data)  +"today " +  TimeUnit.MILLISECONDS.toDays(time));
                    Log.e(TAG , "start_dif "+ start_dif +" end_diff " + end_diff );
                    Log.e(TAG , "daysCount " + alarmEntity.days_count +"");


                    if (end_diff > 0.0  &&  start_dif <0.0  && days > 0){
                        AlarmUtils.cancelAlarm(context,alarmEntity.requestCode);
                        Log.e(TAG , "false");
                        return;
                    }

                }
                dao.updateAlarm(alarmEntity);
                Log.e("Alrmservice" , "" +alarmEntity.toString());
                if (alarmEntity.enable ==1){
                    showNotification(context , extras);
                    wakeLook(context , extras);
                }
            }
        });

    }


}
