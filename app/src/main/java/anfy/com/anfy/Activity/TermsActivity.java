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

public class TermsActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.intell)
    TextView intell;
    @BindView(R.id.terms)
    TextView terms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        title.setText(R.string.terms_conditions);
        StaticDataItem intellItem = StaticData.getData(AppController.STATIC_INDEX_INELECT);
        StaticDataItem termsItem = StaticData.getData(AppController.STATIC_INDEX_TERMS);
        if(intellItem != null) intell.setText(intellItem.getDetails());
        if(termsItem != null) terms.setText(termsItem.getDetails());
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
}
