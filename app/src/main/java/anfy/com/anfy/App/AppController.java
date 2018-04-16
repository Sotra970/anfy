package anfy.com.anfy.App;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.R;

public class AppController extends MultiDexApplication {

    public final static String DUMMY_URL ="https://boygeniusreport.files.wordpress.com/2017/01/iphone-71.jpg?quality=98&strip=all&w=782";
    public final static String IMAGE_URL = "http://bandoraa.net/anfy/uploads/";
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
    @Override
    public void onCreate() {
        super.onCreate();
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


}
