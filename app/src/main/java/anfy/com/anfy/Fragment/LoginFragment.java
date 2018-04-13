package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.TermsActivity;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TextViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.btn_txt)
    TextView btnTxt;
    @BindView(R.id.agree)
    TextView agree;

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
        }
        return mView;
    }

    @OnClick(R.id.btn)
    void next(){
        try {
            ((FragmentSwitchActivity) getActivity()).showFragment(ConfirmPhoneFragment.getInstance());
        }catch (Exception e){}
    }
}
