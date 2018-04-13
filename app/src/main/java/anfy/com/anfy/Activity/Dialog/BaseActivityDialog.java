package anfy.com.anfy.Activity.Dialog;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public abstract class BaseActivityDialog extends AppCompatActivity{

    @Nullable
    @BindView(R.id.d_title)
    TextView title;
    @Nullable
    @BindView(R.id.d_close)
    View close;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected void setDialogTitle(int titleResId){
        if(title != null){
            title.setText(titleResId);
        }
    }

    protected void showCloseButton(boolean show){
        if(close != null){
            close.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Optional
    @OnClick({R.id.d_close, R.id.neg})
    void close(){
        finish();
    }

}
