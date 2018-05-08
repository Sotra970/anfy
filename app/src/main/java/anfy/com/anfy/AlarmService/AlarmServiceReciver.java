package anfy.com.anfy.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;


import java.util.List;

import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.RoomLayer.IkhairDao;
import anfy.com.anfy.App.AppController;

/**
 * Created by developers@appgain.io on 4/26/2018.
 */

public class AlarmServiceReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
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
            IkhairDao dao = DbApi.dao(context) ;
            List<AlarmEntity> alarmEntities = dao.getReminder(extras.getInt("requestCode"));

            if (alarmEntities !=null &&!alarmEntities.isEmpty()){
                AlarmEntity alarmEntity= alarmEntities.get(0);
                Log.e("Alrmservice" , "" +alarmEntity.toString());
                alarmEntity.starting_time = alarmEntity.interval + alarmEntity.starting_time ;
                dao.updateAlarm(alarmEntity);
                Log.e("Alrmservice" , "" +alarmEntity.toString());
                showNotification(context , extras);
                wakeLook(context , extras);
            }
        });

    }


}
