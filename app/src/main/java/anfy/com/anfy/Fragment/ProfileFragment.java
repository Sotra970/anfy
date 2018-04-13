package anfy.com.anfy.Fragment;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends TitledFragment {

    private static final int EDIT_TEXT_ACTIVE_BG = R.color.grey_100;

    private View mView;

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
        }
        return mView;
    }

    private void init() {
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

    @OnClick(R.id.name_edit)
    void activateName(){
        setActive(nameEditText, true);
        setActive(emailEditText, false);
        setActive(phoneEditText, false);
    }

    @OnClick(R.id.email_edit)
    void activateEmail(){
        setActive(nameEditText, false);
        setActive(emailEditText, true);
        setActive(phoneEditText, false);
    }

    @OnClick(R.id.phone_edit)
    void activatePhone(){
        setActive(nameEditText, false);
        setActive(emailEditText, false);
        setActive(phoneEditText, true);
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
}
