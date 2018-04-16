package anfy.com.anfy.Activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TextViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends FragmentSwitchActivity {

    private final static long EXIT_DELAY = 2000L;
    private final static int PERMISSION_INTERNET = 0;

    @BindView(R.id.fragment_welcome)
    View fragment_welcome;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.agree)
    TextView agree;
    @BindView(R.id.btn_txt)
    TextView btnTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if(checkLogin()){
            openActivity(MainActivity.class);
            finish();
            return;
        }
        Glide.with(this).load(R.drawable.splash).into(bg);
        Glide.with(this).load(R.drawable.logo_no_bg).into(logo);
        btnTxt.setText(R.string.continue_no_register);
        TextViewUtils.setMultiColorTextView(
                getString(R.string.you_agree),
                getString(R.string.terms_conditions),
                R.color.def_text_color,
                R.color.colorPrimary,
                new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        openActivity(TermsActivity.class);
                    }
                },
                agree
        );
        Handler ui = new Handler(Looper.myLooper());
        ui.postDelayed(
                () -> {
                    fragment_welcome.setVisibility(View.VISIBLE);
                },
                EXIT_DELAY
        );
    }

    private boolean checkLogin() {
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        return preferenceManager.getUser() != null;
    }

    @OnClick(R.id.btn)
    void noLogin(){
        openActivity(MainActivity.class);
        finish();
    }

    @OnClick(R.id.login)
    void login(){
        openActivity(LoginActivity.class);
        finish();
    }


}
