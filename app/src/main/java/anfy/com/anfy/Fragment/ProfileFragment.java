package anfy.com.anfy.Fragment;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import anfy.com.anfy.Activity.Base.UplodaImagesActivity;
import anfy.com.anfy.Activity.ConfirmPhoneActivity;
import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends TitledFragment {

    private static final int REQUEST_EDIT = 0;

    private static final int EDIT_TEXT_ACTIVE_BG = R.color.grey_100;

    private View mView;

    @BindView(R.id.prof_image)
    CircleImageView profileImage;
    @BindView(R.id.name)
    EditText nameEditText;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.phone)
    EditText phoneEditText;
    @BindView(R.id.name_edit)
    ImageView editName;
    @BindView(R.id.email_edit)
    ImageView editEmail;
    @BindView(R.id.phone_edit)
    ImageView editPhone;
    @BindView(R.id.confirm_text)
    TextView confirmText;
    @BindView(R.id.cancel_text)
    TextView cancelText;
    @BindView(R.id.confirm_layout)
    View confirmLayout;

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_profile, container, false);
            ButterKnife.bind(this, mView);
            init();
            bindUser();
            confirmLayout.setVisibility(View.GONE);
        }
        return mView;
    }


    private UserModel userModel;

    private void bindUser() {
        MyPreferenceManager preferenceManager = new MyPreferenceManager(getContext());
        userModel = preferenceManager.getUser();
        if(userModel != null){
            Glide.with(this).load(userModel.getImage()).into(profileImage);
            nameEditText.setText(userModel.getName());
            emailEditText.setText(userModel.getEmail());
            phoneEditText.setText(userModel.getPhone());
        }
    }

    private void init() {
        confirmText.setText(R.string.save);
        cancelText.setText(R.string.cancel);
        confirmLayout.setVisibility(View.GONE);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmLayout.setVisibility(View.VISIBLE );
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmLayout.setVisibility(View.VISIBLE );
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmLayout.setVisibility(View.VISIBLE );
            }
        });
    }

    @Override
    public int getTitleResId() {
        return R.string.profile;
    }

    private boolean phoneActive = false;
    private boolean emailActive = false;
    private boolean nameActive = false;


    @OnClick(R.id.name_edit)
    void activateName(){
       nameActive = !nameActive;
       emailActive = false;
       phoneActive = false;
       refresh();
    }

    @OnClick(R.id.email_edit)
    void activateEmail(){
        emailActive = !emailActive;
        nameActive = false;
        phoneActive = false;
        refresh();
    }

    @OnClick(R.id.phone_edit)
    void activatePhone(){
        phoneActive = !phoneActive;
        nameActive = false;
        emailActive = false;
        refresh();
    }

    private void setActive(EditText editText, boolean active){
       if(active){
           editText.setText("");
           editText.setEnabled(true);
           editText.setBackgroundColor(ResourcesCompat.getColor(getResources(), EDIT_TEXT_ACTIVE_BG, null));
       }else{
           try {
               String text = editText.getText().toString();
               editText.setEnabled(false);
               editText.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
               editText.setText(text);
           }catch (Exception e){
               Log.e("ProfileFrag", e.getMessage());
           }
       }
    }



    @OnClick(R.id.confirm)
    void confirm(){
        if(nameActive) {
            if(!Validation.isEditTextEmpty(nameEditText)){
                String newName = nameEditText.getText().toString();
                confirmPhoneUpdateInfo(ConfirmPhoneActivity.MODE_NAME, userModel.getPhone() , newName);
            }
        }else if(emailActive){
            if(!Validation.isEditTextEmpty(emailEditText) && Validation.isEmailValid(emailEditText)){
                String newEmail = emailEditText.getText().toString();
                confirmPhoneUpdateInfo(ConfirmPhoneActivity.MODE_EMAIL, userModel.getPhone() , newEmail);
            }
        }else if(phoneActive){
            if(!Validation.isEditTextEmpty(phoneEditText) && Validation.validatePhone(phoneEditText)){
                String newPhone = phoneEditText.getText().toString();
                confirmPhoneUpdateInfo(ConfirmPhoneActivity.MODE_PHONE, newPhone , newPhone);
            }
        }

    }

    @OnClick(R.id.cancel)
    void cancel(){
        init();
        bindUser();
        deactivateAll();
    }

    private void deactivateAll(){
        setActive(nameEditText, false);
        editName.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        setActive(emailEditText, false);
        editEmail.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        setActive(phoneEditText, false);
        editPhone.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        confirmLayout.setVisibility(View.GONE);
        bindUser();
        confirmLayout.setVisibility(View.GONE);
    }

    private void refresh(){
        if(phoneActive){
            setActive(phoneEditText, true);
            editPhone.setImageResource(R.drawable.ic_close_black_24dp);
            resetEmail();
            resetName();
        }else if(emailActive){
            setActive(emailEditText, true);
            editEmail.setImageResource(R.drawable.ic_close_black_24dp);
            resetName();
            resetPhone();
        }else if(nameActive){
            setActive(nameEditText, true);
            editName.setImageResource(R.drawable.ic_close_black_24dp);
            resetPhone();
            resetEmail();
        }else{
            deactivateAll();
        }
    }

    private void resetEmail(){
        setActive(emailEditText, false);
        emailEditText.setText(userModel.getEmail());
        editEmail.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        emailActive = false;
    }

    private void resetPhone(){
        setActive(phoneEditText, false);
        phoneEditText.setText(userModel.getPhone());
        editPhone.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        phoneActive = false;
    }

    private void resetName(){
        setActive(nameEditText, false);
        nameEditText.setText(userModel.getName());
        editName.setImageResource(R.drawable.ic_mode_edit_black_36dp);
        nameActive = false;
    }


    public void confirmPhoneUpdateInfo(int mode, String phone, String infoToUpdate){
        ConfirmPhoneActivity.setMode(mode);
        ConfirmPhoneActivity.setUserId(getUserId());
        ConfirmPhoneActivity.setPhone(phone);
        ConfirmPhoneActivity.setInfoToUpdate(infoToUpdate);
        openActivityForRes(ConfirmPhoneActivity.class, REQUEST_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_EDIT){
            init();
            deactivateAll();
            try {
                ((MainActivity) getActivity()).initNavDrawer();
            }catch (Exception e){}
        }
    }

    @OnClick(R.id.prof_image_container)
    void changeImage(){
        getUploadActivity().pick_image_permission(1, new UplodaImagesActivity.onUploadResponse() {
            @Override
            public void onSuccess(ArrayList<String> imgs_urls) {
                if (imgs_urls != null){
                    Log.e("file[]" , imgs_urls.toString());
                    change_image_request(AppController.IMAGE_URL+imgs_urls.get(0));

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private UplodaImagesActivity getUploadActivity() {
        return (UplodaImagesActivity) getActivity();
    }
    private MainActivity getMAinActivity() {
            return (MainActivity) getActivity();
        }

    private void change_image_request(final  String s) {
        showLoading(true);
        Call<ResponseBody> call = Injector.Api().changeImage(AppController.getUserId(), s);
        call.enqueue(new CallbackWithRetry<ResponseBody>(
                call,
                () -> {
                    showNoInternet(true, (v) -> {
                        showNoInternet(false, null);
                        change_image_request(s);

                    });
                }
        ) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    UserModel userModel =new MyPreferenceManager(getContext()).getUser() ;
                    userModel.setImage(s);
                    new MyPreferenceManager(getContext()).storeUser(userModel);
                    Glide.with(getActivity()).load(userModel.getImage()).into(profileImage);
                    getMAinActivity().refreshImage();
                }
                showLoading(false);

            }
        });
    }
}
