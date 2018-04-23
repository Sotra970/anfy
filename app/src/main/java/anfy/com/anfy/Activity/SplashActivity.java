package anfy.com.anfy.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.logo)
    View logo  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this) ;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkLogin()){
                    startLogin();
                }else {
                    openActivity(MainActivity.class);
                    finish();
                }
            }
        },3000);

    }

    private void startLogin() {
        ActivityOptionsCompat optionsCompat =  ActivityOptionsCompat
                .makeSceneTransitionAnimation(SplashActivity.this,logo,getString(R.string.logo_share_transtion));
        Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        finish();
    }

    private boolean checkLogin() {
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        return preferenceManager.getUser() == null;
    }
}
