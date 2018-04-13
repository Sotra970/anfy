package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.Activity.TermsActivity;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TextViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmPhoneFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.btn_txt)
    TextView btnTxt;

    public static ConfirmPhoneFragment getInstance() {
        return new ConfirmPhoneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_confirm_phone, container, false);
            ButterKnife.bind(this, mView);
            btnTxt.setText(R.string.confirm);
        }
        return mView;
    }

    @OnClick(R.id.resend)
    void resend(){
    }

    @OnClick(R.id.btn)
    void login(){
        openActivity(MainActivity.class);
    }
}
