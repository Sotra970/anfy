package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Model.StaticDataItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.StaticData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivacyAgreementActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.privacy)
    TextView privacy;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_agreement);
        ButterKnife.bind(this);
        title.setText(R.string.privacy_agreement);
        StaticDataItem privacyITem = StaticData.getData(AppController.STATIC_INDEX_PRIVACY);
        if(privacyITem != null){
            privacy.setText(privacyITem.getDetails());
        }
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
}
