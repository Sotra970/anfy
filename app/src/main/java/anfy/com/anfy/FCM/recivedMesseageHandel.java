package anfy.com.anfy.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import anfy.com.anfy.Activity.SplashActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.R;

/**
 * Created by Sotraa on 6/15/2016.
 */
public class recivedMesseageHandel {
    Context context ;
    public recivedMesseageHandel(Context context, RemoteMessage remoteMessage){
        this.context = context.getApplicationContext() ;

    try {

        if (remoteMessage.getData().get("action").equals("notification")){

            sendNotification(remoteMessage.getData().get("message"));
        }
    }catch (Exception e){
        Log.e("receive fcm exception",e.toString());
    }

    }


//
//
    private void sendNotification(String mes) {
        inc_noti();
        Intent intent = new Intent(context, SplashActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"anfy")
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(mes)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority( NotificationCompat.PRIORITY_DEFAULT)
                ;


        final Notification notification = notificationBuilder.build();

        long id =  Calendar.getInstance().getTimeInMillis() ;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) id,notification);
//
    }

    void inc_noti(){
        AppController.getInstance().getPRefrenceManger().INCREMENT_NOTFICATiON();
        Intent intent = new Intent() ;
        intent.setAction(MyPreferenceManager.KEY_INCREMENT_NOTFICATiON);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}