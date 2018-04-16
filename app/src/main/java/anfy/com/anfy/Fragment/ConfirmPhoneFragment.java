package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.MainActivity;
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
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPhoneFragment extends BaseFragment {

    private View mView;

    private final static int MODE_REGISTER = 0;
    private final static int MODE_UPDATE = 1;

    @BindView(R.id.btn_txt)
    TextView btnTxt;
    @BindView(R.id.code)
    EditText code;

    private int userId;
    private String phone;
    private int mMode = -1;

    private UserModel oldUser;
    private UserModel newUser;

    public static ConfirmPhoneFragment getInstance(int userId, String phone) {
        ConfirmPhoneFragment fragment = new ConfirmPhoneFragment();
        fragment.userId = userId;
        fragment.phone = phone;
        fragment.mMode = MODE_REGISTER;
        return fragment;
    }

    public static ConfirmPhoneFragment getInstance(UserModel oldUser, UserModel newUser) {
        ConfirmPhoneFragment fragment = new ConfirmPhoneFragment();
        fragment.oldUser = oldUser;
        fragment.newUser = newUser;
        fragment.phone = newUser.getPhone() == null ? oldUser.getPhone() : newUser.getPhone();
        fragment.mMode = MODE_UPDATE;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_confirm_phone, container, false);
            ButterKnife.bind(this, mView);
            btnTxt.setText(R.string.confirm);
            requestVerify();
        }
        return mView;
    }

    @OnClick(R.id.resend)
    void requestVerify(){
        Call<ResponseBody> call = Injector.Api().sendVerfication(phone, userId);
        call.enqueue(new CallbackWithRetry<ResponseBody>(
                call,
                () -> {
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        requestVerify();
                    });
                }
        ) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showLoading(false);
                }else{
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        requestVerify();
                    });
                }
            }
        });

    }

    @OnClick(R.id.btn)
    void verify(){
        if(!Validation.isEditTextEmpty(code)){
            String ver = code.getEditableText().toString();
            showLoading(true);
            Call<UserModel> call = Injector.Api().verifyCode(userId, ver);
            call.enqueue(new CallbackWithRetry<UserModel>(
                    call,
                    () -> {
                        showNoInternet(true, (v) -> {
                            showNoInternet(false, null);
                            verify();
                        });
                    }
            ) {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()){
                        UserModel userModel = response.body();
                        MyPreferenceManager preferenceManager = new MyPreferenceManager(getContext());
                        preferenceManager.storeUser(userModel);
                        showLoading(false);
                        login();

                    }else{
                        Toast.makeText(getContext(), R.string.ver_error, Toast.LENGTH_SHORT).show();
                        code.setText("");
                        showLoading(false);
                    }

                }
            });
        }
    }

    void login(){
        openActivity(MainActivity.class);
    }

    private void updatePhone(String phone, String verify, CallbackWithRetry<ResponseBody> callback){
        Call<ResponseBody> call = Injector.Api().changePhone(getUserId(), phone, verify);
        call.enqueue(callback);
    }

    private void updateEmail(String email, String verify, CallbackWithRetry<ResponseBody> callback){
        Call<ResponseBody> call = Injector.Api().changeEmail(getUserId(), email, verify);
        call.enqueue(callback);
    }

    private void updateName(String name, String verify, CallbackWithRetry<ResponseBody> callback){
        Call<ResponseBody> call = Injector.Api().changeName(getUserId(), name, verify);
        call.enqueue(callback);
    }


    private void verifyUpdate(){
        ArrayList<Runnable> runnables = new ArrayList<>();
        if(newUser.getName() != null && !Objects.equals(newUser.getName(), oldUser.getName())){
            runnables.add(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

}
