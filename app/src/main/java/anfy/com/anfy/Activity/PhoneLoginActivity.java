package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Fragment.PhoneLoginFragment;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneLoginActivity extends FragmentSwitchActivity {

    private static ArrayList<CountryItem> countryItems;

    public static void setCountryItems(ArrayList<CountryItem> countryItems) {
        PhoneLoginActivity.countryItems = countryItems;
    }

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);
        title.setText(R.string.login);
        showFragment(PhoneLoginFragment.getInstance(countryItems));
    }

    @OnClick(R.id.close)
    void close(){
        openActivity(LoginActivity.class);
        finish();
    }
}

