package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivacyAgreementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_agreement);
        ButterKnife.bind(this);
        title.setText(R.string.privacy_agreement);
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
}
