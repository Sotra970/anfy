package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Dialog.SuggestionActivity;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        textView.setText(R.string.settings_contact_us);
    }

    @OnClick(R.id.suggest)
    void suggest(){
        if(getUser() == null){
            Toast.makeText(this, R.string.sign_sugg, Toast.LENGTH_SHORT).show();
        }else{
            openActivity(SuggestionActivity.class);
        }
    }

    @OnClick(R.id.call)
    void call(){
        Utils.callPhone(getString(R.string.our_phone), this);
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
}
