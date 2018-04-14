package anfy.com.anfy.Service;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.App.AppController;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * @author Annyce Davis
 */
public class Injector
{
    public final static String BASE_URL = "http://bandoraa.net/anfy/mobapi/public/index.php/api/";

    private static final String CACHE_CONTROL = "Cache-Control";
    public  static  int Retry_count = 3 ;
    public  static  int Retry_Time_Offset = 10000 ;

    private final static int UPLOAD_CONNECT_TIME_OUT = 0;


    public static Retrofit provideRetrofit (String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideOkHttpClient() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }



    private static OkHttpClient provideOkHttpClient ()
    {
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )
                .connectTimeout(Retry_Time_Offset, TimeUnit.MILLISECONDS)
                .writeTimeout(Retry_Time_Offset, TimeUnit.MILLISECONDS)
                .readTimeout(Retry_Time_Offset, TimeUnit.MILLISECONDS)
                .build();
    }

    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File(AppController.getInstance().getCacheDir(), "http-cache" ),
                               10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e)
        {
            Log.e("http", e+ " Could not create Cache!" );
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                      Log.e("http", message );
                    }
                } );
        httpLoggingInterceptor.setLevel(  BODY  );
        return httpLoggingInterceptor;
    }

    public static Interceptor provideCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge( 2, TimeUnit.MINUTES )
                        .build();

                return response.newBuilder()
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }


    public static  ApiInterface Api ()
    {
        return provideRetrofit( BASE_URL ).create( ApiInterface.class );
    }

    public static  UploadFileService UploadApi ()
    {
        return provideUploadingRetrofit( BASE_URL ).create( UploadFileService.class );
    }


    private static Retrofit provideUploadingRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideUploadingOkHttpClient() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }

    private static OkHttpClient provideUploadingOkHttpClient() {
        // no time out
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )
                .connectTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(UPLOAD_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(UPLOAD_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
    }

}
