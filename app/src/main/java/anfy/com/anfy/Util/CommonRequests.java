package anfy.com.anfy.Util;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.ResponseListener;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CommonRequests {

    public static void addFav(int user_id, int article_id, Runnable runnable){
        Call<ResponseBody> call = Injector.Api().addFav(user_id, article_id);
        call.enqueue(new CallbackWithRetry<ResponseBody>(
                call,
                new onRequestFailure() {
                    @Override
                    public void onFailure() {
                        addFav(user_id, article_id, runnable);
                    }
                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    runnable.run();
                }
            }
        });
    }

    public static void removeFav(int user_id, int article_id, Runnable runnable){
        Call<ResponseBody> call = Injector.Api().removeFav(user_id, article_id);
        call.enqueue(new CallbackWithRetry<ResponseBody>(
                call,
                () -> addFav(user_id, article_id, runnable)
        ) {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    runnable.run();
                }
            }
        });
    }
}
