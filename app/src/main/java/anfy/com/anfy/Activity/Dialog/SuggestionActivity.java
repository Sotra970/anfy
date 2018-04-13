package anfy.com.anfy.Activity.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import anfy.com.anfy.R;
import butterknife.ButterKnife;

public class SuggestionActivity extends BaseActivityDialog {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_msg);
        ButterKnife.bind(this);
        showCloseButton(true);
        setDialogTitle(R.string.send_suggestion);
    }
}
