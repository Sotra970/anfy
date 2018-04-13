package anfy.com.anfy.Activity.Dialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import anfy.com.anfy.R;
import butterknife.ButterKnife;

public class RequestConsultActivity extends BaseActivityDialog{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_request_consult);
        ButterKnife.bind(this);
        showCloseButton(true);
        setDialogTitle(R.string.request_consult);
    }
}
