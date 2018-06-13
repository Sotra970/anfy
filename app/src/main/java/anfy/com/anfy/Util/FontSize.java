package anfy.com.anfy.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import anfy.com.anfy.App.MyPreferenceManager;

public class FontSize {

    private static final String SIZE_KEY = "SIZE_KEY";
    private static final int DEF_SIZE = 16;
    private static final int MIN_SIZE = 12;
    private static final int MAX_SIZE = 20;
    private static SharedPreferences sharedPreferences;
    private static int size = 0;

    private static int getSize(Context context){
        if(size == 0){
            initPref(context);
            size = sharedPreferences.getInt(SIZE_KEY, DEF_SIZE);
        }
        return size;
    }

    public static boolean increaseFontSize(Context context){
        Log.e("texSize", "post =="+ size);
        getSize(context);
        if(size < MAX_SIZE){
            size++;
            saveSize(context, size);
            Log.e("texSize", "pre =="+ size);
            return true;
        }
        return false;
    }

    public static boolean decreaseFontSize(Context context){
        Log.e("texSize", "post =="+ size);
        getSize(context);
        if(size > MIN_SIZE){
            size--;
            saveSize(context, size);
            Log.e("texSize", "pre =="+ size);
            return true;
        }
        return false;
    }

    private static void saveSize(Context context, int size){
        initPref(context);
        sharedPreferences.edit().putInt(SIZE_KEY, size).apply();
    }

    private static void initPref(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("FONT_SIZE", Context.MODE_PRIVATE);
        }
    }

    public static int getHeaderSize(Context context){
        return getSize(context) + 1;
    }

    public static int getBodySize(Context context){
        return getSize(context);
    }
}
