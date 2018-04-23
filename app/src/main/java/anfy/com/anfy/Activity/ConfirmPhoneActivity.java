package anfy.com.anfy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ConfirmPhoneActivity extends BaseActivity {


    public final static int MODE_REGISTER = 0;
    public final static int MODE_NAME = 1;
    public final static int MODE_EMAIL = 2;
    public final static int MODE_PHONE = 3;

    @BindView(R.id.btn_txt)
    TextView btnTxt;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.send_phone)
    TextView sendPhone;


    private static int userId;
    private static String phone;
    private static int mMode = -1;

    private static String infoToUpdate;

    public static void setUserId(int userId) {
        ConfirmPhoneActivity.userId = userId;
    }

    public static void setPhone(String phone) {
        ConfirmPhoneActivity.phone = phone;
    }

    public static void setMode(int mMode) {
        ConfirmPhoneActivity.mMode = mMode;
    }

    public static void setInfoToUpdate(String infoToUpdate) {
        ConfirmPhoneActivity.infoToUpdate = infoToUpdate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
        ButterKnife.bind(this);
        title.setText(R.string.confirm_phone);
        sendPhone.setText(phone);
        btnTxt.setText(R.string.confirm);
        requestVerify(false);
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
    
    // remove on release
    private void verify(){
        
    }

    @OnClick(R.id.resend)
    void resend(){
        requestVerify(true);
    }


    void requestVerify(boolean retry){
        showLoading(true);
        Call<ResponseBody> call = Injector.Api().sendVerfication(phone, userId);
        call.enqueue(new CallbackWithRetry<ResponseBody>(
                call,
                () -> {
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        requestVerify(retry);
                    });
                }
        ) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(retry){
                        Toast.makeText(ConfirmPhoneActivity.this, R.string.code_resent, Toast.LENGTH_SHORT).show();
                    }
                    showLoading(false);
                }else{
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        requestVerify(retry);
                    });
                }
            }
        });

    }

    void verifyRegister(){
        if(!Validation.isEditTextEmpty(code)){
            MyPreferenceManager preferenceManager = new MyPreferenceManager(ConfirmPhoneActivity.this);
            String ver = code.getEditableText().toString();
            showLoading(true);
            Call<UserModel> call = Injector.Api().verifyCode(userId, ver);
            call.enqueue(new CallbackWithRetry<UserModel>(
                    call,
                    () -> {
                        showNoInternet(true, (v) -> {
                            showNoInternet(false, null);
                            verifyRegister();
                        });
                    }
            ) {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()){
                        UserModel userModel = response.body();
                        preferenceManager.storeUser(userModel);
                        showLoading(false);
                        login();

                    }else{
                        Toast.makeText(ConfirmPhoneActivity.this, R.string.ver_error, Toast.LENGTH_SHORT).show();
                        code.setText("");
                        showLoading(false);
                    }

                }
            });
        }
    }

    void login(){
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openActivity(MainActivity.class);
        finish();
    }

    private void update(){
        if(!Validation.isEditTextEmpty(code)) {
            String ver = code.getEditableText().toString();
            Call<ResponseBody> call = null;
            switch (mMode){
                case MODE_NAME:
                    call = Injector.Api().changeName(userId, infoToUpdate, ver);
                    break;
                case MODE_EMAIL:
                    call = Injector.Api().changeEmail(userId, infoToUpdate, ver);
                    break;
                case MODE_PHONE:
                    call = Injector.Api().changePhone(userId, infoToUpdate, ver);
                    break;
                default:
                    return;
            }
            if(call != null){
                MyPreferenceManager preferenceManager = new MyPreferenceManager(ConfirmPhoneActivity.this);
                UserModel userModel = preferenceManager.getUser();
                showLoading(true);
                call.enqueue(new CallbackWithRetry<ResponseBody>(
                        call,
                        new onRequestFailure() {
                            @Override
                            public void onFailure() {
                                showNoInternet(true,
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showNoInternet(false, null);
                                                update();
                                            }
                                        });
                            }
                        }
                ) {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            switch (mMode){
                                case MODE_NAME:
                                    userModel.setName(infoToUpdate);
                                    break;
                                case MODE_EMAIL:
                                    userModel.setEmail(infoToUpdate);
                                    break;
                                case MODE_PHONE:
                                    userModel.setPhone(infoToUpdate);
                                    break;
                            }
                            preferenceManager.storeUser(userModel);
                            Toast.makeText(ConfirmPhoneActivity.this, R.string.profile_update, Toast.LENGTH_SHORT).show();
                            code.setText("");
                            showLoading(false);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(ConfirmPhoneActivity.this, R.string.ver_error, Toast.LENGTH_SHORT).show();
                            code.setText("");
                            showLoading(false);
                        }
                    }
                });
            }
        }
    }

    @OnClick(R.id.btn)
    void verifyCode(){
        if(mMode == MODE_REGISTER){
            verifyRegister();
        }else{
            update();
        }
    }
}
