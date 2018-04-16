package anfy.com.anfy.Fragment;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;


import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends TitledFragment {

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
        }
        return mView;
    }


    private UserModel userModel;

    private void bindUser() {
        MyPreferenceManager preferenceManager = new MyPreferenceManager(getContext());
        userModel = preferenceManager.getUser();
        if(userModel != null){
            Glide.with(this).load(Utils.getImageUrl(userModel.getImage())).into(profileImage);
            nameEditText.setText(userModel.getName());
            emailEditText.setText(userModel.getEmail());
            phoneEditText.setText(userModel.getPhone());
        }
    }

    private void init() {
        confirmText.setText(R.string.save);
        cancelText.setText(R.string.cancel);
        confirmLayout.setVisibility(View.GONE);
        nameEditText.setOnEditorActionListener((v, actionId, event) -> {
            setActive(nameEditText, false);
            return true;
        });
        emailEditText.setOnEditorActionListener((v, actionId, event) -> {
            setActive(emailEditText, false);
            return true;
        });
        phoneEditText.setOnEditorActionListener((v, actionId, event) -> {
            setActive(phoneEditText, false);
            return true;
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
       if(nameActive){
           deactivateAll();
       }else{
           setActive(nameEditText, true);
           setActive(phoneEditText, false);
           phoneEditText.setText(userModel.getPhone());
           setActive(emailEditText, false);
           emailEditText.setText(userModel.getEmail());
           confirmLayout.setVisibility(View.VISIBLE);
       }
       nameActive = !nameActive;
       editName.setImageResource(!nameActive ? R.drawable.ic_mode_edit_black_36dp : R.drawable.ic_close_black_24dp);
    }

    @OnClick(R.id.email_edit)
    void activateEmail(){
        if(emailActive){
            deactivateAll();
        }else{
            setActive(nameEditText, false);
            nameEditText.setText(userModel.getName());
            setActive(emailEditText, true);
            setActive(phoneEditText, false);
            phoneEditText.setText(userModel.getPhone());
            confirmLayout.setVisibility(View.VISIBLE);
        }
        emailActive = !emailActive;
        editEmail.setImageResource(!emailActive ? R.drawable.ic_mode_edit_black_36dp : R.drawable.ic_close_black_24dp);
    }

    @OnClick(R.id.phone_edit)
    void activatePhone(){
        if(phoneActive){
            deactivateAll();
        }else{
            setActive(nameEditText, false);
            nameEditText.setText(userModel.getName());
            setActive(emailEditText, false);
            emailEditText.setText(userModel.getEmail());
            setActive(phoneEditText, true);
            confirmLayout.setVisibility(View.VISIBLE);
        }
        phoneActive = !phoneActive;
        editPhone.setImageResource(!phoneActive ? R.drawable.ic_mode_edit_black_36dp : R.drawable.ic_close_black_24dp);
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



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK && data!=null) {
            super.onActivityResult(requestCode , resultCode , data);
        }
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data!=null) {
            photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
        }
    }*/

    /*private void selectPhoto(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppController.PERMISSION_REQUEST_STORAGE);
        }
        else {
            pick_img(1);
        }
    }

    private ArrayList<String> photoPaths;
    private ArrayList<File> photos_result_uri_array  = new ArrayList<>();


    private void pick_img(int count){
        photoPaths = new ArrayList<>() ;
        photos_result_uri_array = new ArrayList<>() ;
        FilePickerBuilder
                .getInstance()
                .setMaxCount(count)
                .setSelectedFiles(photoPaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }*/

    @OnClick(R.id.confirm)
    void confirm(){

    }

    @OnClick(R.id.cancel)
    void cancel(){
        init();
        bindUser();
    }

    private void deactivateAll(){
        setActive(nameEditText, false);
        setActive(emailEditText, false);
        setActive(phoneEditText, false);
        confirmLayout.setVisibility(View.GONE);
        bindUser();
    }

    private void refresh(){
        if(phoneActive){
            setActive(phoneEditText, true);
            resetEmail();
            resetName();
        }else if(emailActive){
            setActive(emailEditText, true);
            resetName();
            resetPhone();
        }else if(nameActive){
            setActive(nameEditText, true);
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
}
