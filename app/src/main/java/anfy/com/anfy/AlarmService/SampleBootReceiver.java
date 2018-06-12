package anfy.com.anfy.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.RoomLayer.AnfyDao;
import anfy.com.anfy.R;


/**
 * Created by developers@appgain.io on 5/1/2018.
 */

public class SampleBootReceiver extends BroadcastReceiver {
//    adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SampleBootReceiver" , intent.getAction());
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmAsyncTask(context).execute("");
        }
    }


    private static class AlarmAsyncTask extends AsyncTask<String, Void, String> {
        Context context ;

        public AlarmAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            AnfyDao ikhairDao = DbApi.dao(context) ;
            List<AlarmEntity> reminderModels = ikhairDao.getRemindersList();
            for (AlarmEntity alarmEntity: reminderModels) {
                Bundle bundle = new Bundle();
                bundle.putString("message" ,context.getString(R.string.havind_reminder) +" " + alarmEntity.uiModelName + "("+context.getString(R.string.take)+(alarmEntity.take_number+1)+")");

                bundle.putString("message" , context.getString(R.string.havind_reminder) +" " + alarmEntity.uiModelName);
                alarmEntity.starting_time = checkAlarmTime(alarmEntity);
                AlarmUtils.setAlarm(context ,alarmEntity.requestCode  , alarmEntity.starting_time, alarmEntity.interval ,bundle);
            }
            return "";

        }

        private long checkAlarmTime(AlarmEntity alarmEntity ) {
            long diff = Calendar.getInstance().getTimeInMillis() - alarmEntity.starting_time;
            if (diff > 0){
                alarmEntity.starting_time+= alarmEntity.interval ;
                checkAlarmTime(alarmEntity);
            }else {
                return alarmEntity.starting_time;
            }
            return alarmEntity.starting_time;
        }
    }
}
