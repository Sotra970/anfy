package anfy.com.anfy.Activity.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenderDialog extends BaseActivityDialog {

    public final static String KEY_GENDER = "_GENDER";

    @BindView(R.id.d_title)
    TextView dialogTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gender);
        ButterKnife.bind(this);
        dialogTitle.setText(R.string.gender);
    }

    @OnClick(R.id.male)
    void selectMale(){
        Intent intent = new Intent();
        intent.putExtra(KEY_GENDER, getString(R.string.male));
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.female)
    void selectFemale(){
        Intent intent = new Intent();
        intent.putExtra(KEY_GENDER, getString(R.string.female));
        setResult(RESULT_OK, intent);
        finish();
    }
}
