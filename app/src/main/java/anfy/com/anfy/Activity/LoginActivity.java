package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Fragment.LoginFragment;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

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

