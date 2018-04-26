package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.Base.SocialActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.SocialUser;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.TextViewUtils;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends SocialActivity implements SocialActivity.socialLoginInterface {

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

    @BindView(R.id.country_image)
    ImageView cImage;
    @BindView(R.id.country_code)
    TextView cText;

    private ArrayList<CountryItem> countryItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSocialLoginInterface(this);
        if(checkLogin()){
            openActivity(MainActivity.class);
            finish();
            return;
        }
//        Glide.with(this).load(R.drawable.splash).into(bg);
//        Glide.with(this).load(R.drawable.logo_no_bg).into(logo);
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
        loadCountries();
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
        openActivity(PhoneLoginActivity.class);
        finish();
    }

    private void loadCountries(){
        showLoading(true);
        Call<ArrayList<CountryItem>> call =
                Injector.Api().countries();
        call.enqueue(new CallbackWithRetry<ArrayList<CountryItem>>(
                call,
                () -> {
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCountries();
                    });
                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CountryItem>> call, @NonNull Response<ArrayList<CountryItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<CountryItem> countryItems = response.body();
                    PhoneLoginActivity.setCountryItems(countryItems);
                    if(countryItems != null && !countryItems.isEmpty()){
                        CountryItem countryItem = countryItems.get(0);
                        Glide.with(LoginActivity.this).load(Utils.getImageUrl(countryItem.getIcon())).into(cImage);
                        cText.setText(countryItem.getPhoneCode());
                    }
                }else{
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCountries();
                    });
                }
                showLoading(false);
            }
        });
    }


    @OnClick(R.id.twitter_icon)
    void twitter_icon_on_click(){
       twitter_on_click();
    }

    @OnClick(R.id.facebook_icon)
    void facebook_icon_on_click()
    {
        facebook_on_click();
    }


    @OnClick(R.id.google_icon)
    void google_icon_on_click(){
        google_on_click();
    }


    @Override
    public void social_login(SocialUser user) {
        showLoading(false);
        socialLoginReq(user);
    }

    private void socialLoginReq(final SocialUser user) {

        showLoading(true);
        Call<UserModel> call = Injector.Api().signupWithSocialAccount(
                user.getType(),
                user.getUid()
        );
        call.enqueue(new CallbackWithRetry<UserModel>(
                call,
                () -> {
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        socialLoginReq(user);
                    });
                }
        ) {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    UserModel userModel = response.body();
                    if(userModel != null){
                        new MyPreferenceManager(getApplicationContext())
                                .storeUser(userModel);
                       AppController.restart();
                    }
                    showLoading(false);
                }else{
                    showLoading(false);
                }
            }
        });
    }

}
