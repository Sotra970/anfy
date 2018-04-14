package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Dialog.SuggestionActivity;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.suggest)
    void suggest(){
        openActivity(SuggestionActivity.class);
    }

    @OnClick(R.id.call)
    void call(){
        Utils.callPhone(getString(R.string.our_phone), this);
    }
}
