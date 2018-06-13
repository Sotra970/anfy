package anfy.com.anfy.App;

import android.content.ComponentName;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.Activity.SplashActivity;
import anfy.com.anfy.AlarmService.RoomLayer.AnfyDatabase;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.UserModel;

public class AppController extends MultiDexApplication {

    public final static String DUMMY_URL ="https://boygeniusreport.files.wordpress.com/2017/01/iphone-71.jpg?quality=98&strip=all&w=782";
    public final static String IMAGE_URL = "http://anfy.net/anfy/uploads/";
    public final static String TEMP_IMAGE_URL = IMAGE_URL+"temp.png";

    public static final int STATIC_INDEX_PRIVACY = 1;
    public static final int STATIC_INDEX_TERMS = 2;
    public static final int STATIC_INDEX_INELECT = 3;
    public static final int STATIC_INDEX_PHONE = 4;
    public static final int STATIC_INDEX_FAX = 5;
    public static final int STATIC_INDEX_EMAIL = 6;
    public static final int STATIC_INDEX_FACEBOOK = 7;
    public static final int STATIC_INDEX_GOOGLE = 8;
    public static final int STATIC_INDEX_YOUTUBE = 9;
    public static final int STATIC_INDEX_TWITTER = 10;
    public static final int STATIC_INDEX_LINKED_IN = 11;
    public static final int STATIC_INDEX_MISSION = 12;
    public static final int STATIC_INDEX_VISION = 13;

    public final static int NO_USER_ID = -1;

    public static final int REQUEST_COUNTRY = 0;
    public static final int REQUEST_CITY = 1;

    public static final int PERMISSION_REQUEST_STORAGE = 99;

    public final static int MIN_AGE = 1;
    public final static int MAX_AGE = 100;

    private static ArrayList<CountryItem> countryItems;

    public static ArrayList<CountryItem> getCountryItems() {
        return countryItems;
    }

    public static void setCountryItems(ArrayList<CountryItem> countryItems) {
        AppController.countryItems = countryItems;
    }

    private static AppController mInstance;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static long getTimeStamp() {
        long time  = Calendar.getInstance().getTimeInMillis() ;
        long timeStamp = TimeUnit.MILLISECONDS.toSeconds(time);
        return  timeStamp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this ;
        MultiDex.install(this);
    }

    private static ExecutorService executorService;

    public static ExecutorService getExecutorService(){
        if(executorService == null){
            int cpuNum = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(cpuNum);
        }
        return executorService;
    }


    public static int getUserId() {
        UserModel userModel = new MyPreferenceManager(getInstance().getApplicationContext()).getUser() ;
        if (userModel ==null){
            return  -2  ;
        }else {
            return  userModel.getId();
        }
    }

    public static void restart() {
        Intent intent = new Intent(getInstance().getApplicationContext(), SplashActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(cn);
        getInstance().startActivity(mainIntent);
    }

    public static AnfyDatabase getDb() {
        return DbApi.db(getInstance());
    }

    public MyPreferenceManager getPRefrenceManger() {
        return new MyPreferenceManager(getApplicationContext());
    }
}
