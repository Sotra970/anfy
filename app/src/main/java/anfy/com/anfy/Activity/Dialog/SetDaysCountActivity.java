package anfy.com.anfy.Activity.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

import anfy.com.anfy.R;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetDaysCountActivity extends BaseActivityDialog {

    @BindView(R.id.confirm_text)
    TextView conTxt;
    @BindView(R.id.cancel_text)
    TextView cnclTxt;

    @BindView(R.id.count)
    EditText count;
    static  DaysCountCallBback  daysCountCallBback ;
    public static void  start(Context context , DaysCountCallBback daysCountCallBback ){
        Intent intent = new Intent(context , SetDaysCountActivity.class) ;
        SetDaysCountActivity.daysCountCallBback = daysCountCallBback ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_day_count);
        ButterKnife.bind(this);
        showCloseButton(false);
        setDialogTitle(R.string.set_days_count);
        conTxt.setText(R.string.confirm);
        cnclTxt.setText(R.string.cancel);
    }



    @OnClick(R.id.confirm)
    void confirm(){
        if(!Validation.isEditTextEmpty(count)){
            int days_count  = Integer.valueOf(count.getText().toString()) ;
            daysCountCallBback.onConfirm(days_count);
            finish();
        }
    }

    @Override
    void close() {
        daysCountCallBback.onCancel();
        super.close();
    }

    public  static  interface  DaysCountCallBback extends Serializable {
        void onConfirm(int count);
        void onCancel();
    }

    @Override
    public void onBackPressed() {
        daysCountCallBback.onCancel();
        super.onBackPressed();
    }


    @OnClick(R.id.cancel)
    void cancel(){
        daysCountCallBback.onCancel();
        finish();
    }
}
