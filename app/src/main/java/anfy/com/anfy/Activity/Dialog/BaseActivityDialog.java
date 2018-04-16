package anfy.com.anfy.Activity.Dialog;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public abstract class BaseActivityDialog extends AppCompatActivity{

    @Nullable
    @BindView(R.id.d_title)
    TextView title;
    @Nullable
    @BindView(R.id.d_close)
    View close;
    @Nullable
    @BindView(R.id.no_internet_layout)
    View noInternet;
    @Nullable
    @BindView(R.id.retry)
    View retry;
    @Nullable
    @BindView(R.id.loading)
    View loading;


    public void showNoInternet(boolean show, View.OnClickListener retryClickListener){
        if(noInternet != null && retry != null){
            noInternet.setVisibility(show ? View.VISIBLE : View.GONE);
            retry.setOnClickListener(retryClickListener);
        }
    }

    public void showLoading(boolean show){
        if(loading != null){
            loading.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Nullable
    private UserModel getUser(){
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        return preferenceManager.getUser();
    }

    public int getUserId(){
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        UserModel userModel = preferenceManager.getUser();
        return userModel == null ? AppController.NO_USER_ID : userModel.getId();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected void setDialogTitle(int titleResId){
        if(title != null){
            title.setText(titleResId);
        }
    }

    protected void showCloseButton(boolean show){
        if(close != null){
            close.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Optional
    @OnClick({R.id.d_close, R.id.neg})
    void close(){
        finish();
    }

}
