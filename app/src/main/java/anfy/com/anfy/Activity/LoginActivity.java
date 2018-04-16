package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Fragment.LoginFragment;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends FragmentSwitchActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText(R.string.login);
        showFragment(LoginFragment.getInstance());
    }


}

