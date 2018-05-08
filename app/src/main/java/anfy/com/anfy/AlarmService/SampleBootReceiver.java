package anfy.com.anfy.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.RoomLayer.IkhairDao;
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
            IkhairDao ikhairDao = DbApi.dao(context) ;
            List<AlarmEntity> reminderModels = ikhairDao.getRemindersList();
            for (AlarmEntity alarmEntity: reminderModels) {
                Bundle bundle = new Bundle();
                bundle.putString("message" , context.getString(R.string.havind_reminder) +" " + alarmEntity.uiModelName);
                AlarmUtils.setAlarm(context ,alarmEntity.requestCode  , alarmEntity.starting_time, alarmEntity.interval ,bundle);
            }
            return "";

        }
    }
}
