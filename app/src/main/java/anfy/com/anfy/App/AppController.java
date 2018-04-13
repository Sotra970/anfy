package anfy.com.anfy.App;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import anfy.com.anfy.R;

public class AppController extends Application {

    public final static String DUMMY_URL ="https://boygeniusreport.files.wordpress.com/2017/01/iphone-71.jpg?quality=98&strip=all&w=782";

    public final static int NO_USER_ID = -1;


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
