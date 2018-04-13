package anfy.com.anfy.Service;

import android.os.Handler;
import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by sotra on 5/9/2017.
 */
public abstract class CallbackWithRetry<T> implements Callback<T> {

    public static int HTTP_REQUEST_RETRY_COUNT = 5;
    public static int HTTP_REQUEST_RETRY_INTERVAl_MILISECONDS = 3000;
    private  final int TOTAL_RETRIES ;
    private  final int INTERVAL  ;
    private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;

    onRequestFailure onRequestFailure ;


    public CallbackWithRetry(int TOTAL_RETRIES, int INTERVAL, Call<T> call , onRequestFailure onRequestFailure ) {
        this.TOTAL_RETRIES = TOTAL_RETRIES;
        this.INTERVAL = INTERVAL*2;
        this.call = call;
        this.onRequestFailure = onRequestFailure;
    }

    public CallbackWithRetry(Call<T> call , onRequestFailure onRequestFailure ) {
        this.TOTAL_RETRIES = Injector.Retry_count;
        this.INTERVAL = Injector.Retry_Time_Offset*2;
        this.call = call;
        this.onRequestFailure = onRequestFailure;
    }

    @Override
    public void onFailure(Call<T> call , Throwable t) {

        if (t instanceof SocketTimeoutException){

            if (retryCount++ < TOTAL_RETRIES) {
                Log.e(TAG, "Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
                retry();
            }else{
                onRequestFailure.onFailure();
            }
        }else {
            Log.e(TAG , "onFailure"+ t.toString());
                onRequestFailure.onFailure();
        }
    }

    private void retry() {

        call.cancel();
        new Handler().postDelayed(() -> call.clone().enqueue(CallbackWithRetry.this),INTERVAL);
    }
}
