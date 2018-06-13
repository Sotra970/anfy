package anfy.com.anfy.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Dialog.SuggestionActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Model.StaticDataItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.ShareUtils;
import anfy.com.anfy.Util.StaticData;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView textView;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.fax)
    TextView fax;
    @BindView(R.id.email)
    TextView email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        textView.setText(R.string.settings_contact_us);
        bindInfo();
    }

    private void bindInfo() {
        StaticDataItem phoneItem = StaticData.getData(AppController.STATIC_INDEX_PHONE);
        StaticDataItem emailItem = StaticData.getData(AppController.STATIC_INDEX_EMAIL);
        StaticDataItem faxItem = StaticData.getData(AppController.STATIC_INDEX_FAX);
        if(phoneItem != null){
            phone.setText(phoneItem.getDetails());
        }
        if(emailItem != null){
            email.setText(emailItem.getDetails());
        }
        if(faxItem != null){
            fax.setText(faxItem.getDetails());
        }

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
        StaticDataItem phoneItem = StaticData.getData(AppController.STATIC_INDEX_PHONE);
        if(phoneItem != null){
            Utils.callPhone(phoneItem.getDetails(), this);
        }
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }

    /*@OnClick(R.id.gplus)
    void openGplus(){
            ShareUtils.sendEmail("Anfy2018@gmail.com");

    }


    @OnClick(R.id.twitter)
    void openTwitter(){
            ShareUtils.openUrl("https://twitter.com/anfy2018") ;
    }*/

    @OnClick(R.id.facebook)
    void facebook(){
        StaticDataItem face = StaticData.getData(AppController.STATIC_INDEX_FACEBOOK);
        if(face != null){
            openLink(face.getDetails());
        }
    }

    @OnClick(R.id.utube)
    void youtube(){
        StaticDataItem youtube = StaticData.getData(AppController.STATIC_INDEX_YOUTUBE);
        if(youtube != null){
            openLink(youtube.getDetails());
        }
    }

    @OnClick(R.id.twitter)
    void twitter(){
        StaticDataItem twitter = StaticData.getData(AppController.STATIC_INDEX_TWITTER);
        if(twitter != null){
            openLink(twitter.getDetails());
        }
    }

    @OnClick(R.id.linkedin)
    void linkedin(){
        StaticDataItem linkedin = StaticData.getData(AppController.STATIC_INDEX_LINKED_IN);
        if(linkedin != null){
            openLink(linkedin.getDetails());
        }
    }

    @OnClick(R.id.gplus)
    void gplus(){
        StaticDataItem gplus = StaticData.getData(AppController.STATIC_INDEX_GOOGLE);
        if(gplus != null){
            openLink(gplus.getDetails());
        }
    }

    private void openLink(String url){
        if(url != null){
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

}
