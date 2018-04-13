package anfy.com.anfy.Activity.Base;


import android.content.ComponentName;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import anfy.com.anfy.R;

/**
 * Created by Ahmed on 10/27/2017.
 */

public abstract class FragmentSwitchActivity extends BaseActivity {


    public void showFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    public void showFragment(Fragment fragment , boolean back , int anim_enter  , int exit_anim  ){
        if(fragment != null){

            if (back){
                getSupportFragmentManager()
                        .beginTransaction()
                        //.setCustomAnimations(anim_enter , exit_anim)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }else {
                getSupportFragmentManager()
                        .beginTransaction()
                        //.setCustomAnimations(ENTER_ANIM , LEAVE_ANIM)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }


    public void showFragment(Fragment fragment , String tag  ){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(ENTER_ANIM , LEAVE_ANIM)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    /*public void showFragmentNoAnim(Fragment fragment, String tag, boolean back){
        if(fragment != null){
            if (back){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(0 , 0)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(tag)
                        .commit();
            }else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(0 , 0)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }


    public void showLongToast(View view, String stringId) {
        Snackbar.make(view, stringId, Snackbar.LENGTH_LONG).show();
    }

    public void showLongToast(View view, int stringId) {
        Snackbar.make(view, stringId, Snackbar.LENGTH_LONG).show();

    }

    public void showLongToast(View view, int stringId, int action, View.OnClickListener listener) {
        Snackbar snack = Snackbar.make(view, stringId, Snackbar.LENGTH_LONG)
                .setAction(action, listener)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snack.show();

    }*/

    /*public   void startFadeFinishAc(Intent intent , boolean finish ){
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (finish)
        finish();
    }


    public  void startRestartAc(Intent intent  ){
        ComponentName cn = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(cn);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(mainIntent);

    }*/


}
