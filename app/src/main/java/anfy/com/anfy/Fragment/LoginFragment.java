package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.Dialog.CountryDialog;
import anfy.com.anfy.Activity.TermsActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.TextViewUtils;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.Util.Validation;
import anfy.com.anfy.ViewHolder.CountryVH;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.btn_txt)
    TextView btnTxt;
    @BindView(R.id.agree)
    TextView agree;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.country_code)
    TextView countryCode;
    @BindView(R.id.country_image)
    ImageView countryImage;

    private CountryItem countryItem = null;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.login_fragment, container, false);
            ButterKnife.bind(this, mView);
            btnTxt.setText(R.string.login);
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
        }
        return mView;
    }

    @OnClick(R.id.btn)
    void signin(){
        if(validate()){
            try {
                String ph = phone.getEditableText().toString();
                sendPhone(ph);
            }catch (Exception e){}
        }
    }

    private void sendPhone(String phone){
        if(countryItem != null){
            Call<UserModel> call = Injector.Api().phoneRegister(countryItem.getPhoneCode() + phone, countryItem.getId());
            call.enqueue(new CallbackWithRetry<UserModel>(
                    call,
                    () -> {
                        showNoInternet(true, (v) -> {
                            showNoInternet(false, null);
                            sendPhone(phone);
                        });
                    }
            ) {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()){
                        UserModel userModel = response.body();
                        showLoading(false);
                        try {
                            ((FragmentSwitchActivity) getActivity()).showFragment(ConfirmPhoneFragment.getInstance(userModel.getId(), userModel.getPhone()));
                        }catch (Exception e){}
                    }else{
                        showNoInternet(true, (v) -> {
                            showNoInternet(false, null);
                            sendPhone(phone);
                        });
                    }
                }
            });
        }
       /*if(countryItem != null){
           Call<UserModel> call = Injector.Api().phoneRegister(phone, 1);
           call.enqueue(new CallbackWithRetry<UserModel>(
                   call,
                   () -> {
                       showNoInternet(true, (v) -> {
                           showNoInternet(false, null);
                           sendPhone(phone);
                       });
                   }
           ) {
               @Override
               public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                   if(response.isSuccessful()){
                       UserModel userModel = response.body();
                       showLoading(false);
                       try {
                           ((FragmentSwitchActivity) getActivity()).showFragment(ConfirmPhoneFragment.getInstance(userModel.getId(), userModel.getPhone()));
                       }catch (Exception e){}
                   }else{
                       showNoInternet(true, (v) -> {
                           showNoInternet(false, null);
                           sendPhone(phone);
                       });
                   }
               }
           });
       }*/
    }

    private boolean validate() {
        if (!Validation.isEditTextEmpty(phone))
        {
            return true;
        }else return false ;
    }

    @OnClick(R.id.select_country)
    void select(){
        openActivityForRes(CountryDialog.class, AppController.REQUEST_COUNTRY);
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
            public void onResponse(Call<ArrayList<CountryItem>> call, Response<ArrayList<CountryItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<CountryItem> countryItems = response.body();
                    if(countryItems != null && !countryItems.isEmpty()){
                        countryItem = countryItems.get(0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == AppController.REQUEST_COUNTRY){
                try {
                    CountryItem countryItem = (CountryItem)
                            data.getSerializableExtra(CountryDialog.KEY_COUNTRY_ID);
                    countryCode.setText(countryItem.getPhoneCode());
                    Glide.with(this).load(Utils.getImageUrl(countryItem.getIcon())).into(countryImage);
                }catch (Exception e){}

            }
        }
    }
}
