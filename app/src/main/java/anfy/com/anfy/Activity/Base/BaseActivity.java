package anfy.com.anfy.Activity.Base;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import butterknife.BindView;

public abstract class BaseActivity extends AppCompatActivity{

    @Nullable
    @BindView(R.id.loading)
    View loading;

    protected void openActivity(Class<?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    public void showLoading(boolean show){
        if(loading != null){
            loading.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Nullable
    public UserModel getUser(){
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        return preferenceManager.getUser();
    }

    public int getUserId(){
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        UserModel userModel = preferenceManager.getUser();
        return userModel == null ? AppController.NO_USER_ID : userModel.getId();
    }

}
