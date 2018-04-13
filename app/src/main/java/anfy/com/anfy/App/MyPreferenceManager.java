package anfy.com.anfy.App;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import anfy.com.anfy.Activity.SplashActivity;
import anfy.com.anfy.Model.UserModel;

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Anfy";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER= "KEY_USER";

    public static final String KEY_INCREMENT_NOTFICATiON = "KEY_INCREMENT_NOTFICATiON";




    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(UserModel user) {
        editor.clear();
        editor.commit();

        editor.putLong(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER, new Gson().toJson(user));
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() +"--"  );
    }

    public UserModel getUser() {
        if (pref.getLong(KEY_USER_ID, 0) != 0) {

            String user  = pref.getString(KEY_USER,"");
            if (TextUtils.isEmpty(user)) return  null ;
            return new Gson().fromJson(user,UserModel.class);
        }
        return null;
    }


    public void clear(boolean restart) {
        editor.clear();
        editor.commit();
        if (!restart)return;
        Intent intent = new Intent(_context, SplashActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(cn);
        _context.startActivity(mainIntent);
    }



    public int  get_notfication() {
        int prev = pref.getInt(KEY_INCREMENT_NOTFICATiON, 0);
        return prev;
    }



    public void INCREMENT_NOTFICATiON() {
        int prev = pref.getInt(KEY_INCREMENT_NOTFICATiON, 0);
        prev++ ;
        editor.putInt(KEY_INCREMENT_NOTFICATiON , prev) ;
        editor.commit();
    }

    public void CLEAR_NOTFICATiON() {
        editor.putInt(KEY_INCREMENT_NOTFICATiON , 0) ;
        editor.commit();
    }



}
