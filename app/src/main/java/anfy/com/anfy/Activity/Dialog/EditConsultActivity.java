package anfy.com.anfy.Activity.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import anfy.com.anfy.Activity.ChatActivity;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class EditConsultActivity extends BaseActivityDialog{

    public static final String CONSULT_ITEM = "CONSULT_ITEM";

    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.consult_text)
    EditText consult;
    @BindView(R.id.radio)
    RadioGroup radioGroup;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.female)
    RadioButton female;
    @BindView(R.id.confirm_text)
    TextView confirmText;
    @BindView(R.id.cancel_text)
    TextView cancelText;

    ConsultationItem consultationItem ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_request_consult);
        ButterKnife.bind(this);
        showCloseButton(true);
        setDialogTitle(R.string.request_consult);
        confirmText.setText(R.string.edit);
        cancelText.setText(R.string.cancel);
         consultationItem = (ConsultationItem) getIntent().getExtras().get("model");
        bindConsultation(consultationItem);
    }

    private void bindConsultation(ConsultationItem consultationItem) {
        consult.setText(consultationItem.getDetails());
        age.setText(consultationItem.getAge()+"");
        if (consultationItem.getGender().equals(ConsultationItem.GENDER_FEMALE)){
            female.setChecked(true);
            male.setChecked(false);
        }else {
            female.setChecked(false);
            male.setChecked(true);
        }
    }

    @OnClick(R.id.confirm)
    void createConsult(){
        if(valid()){
            sendCosult();
        }
    }

    private void sendCosult() {
        ConsultationItem consultationItem = getConsult();
        if(consultationItem != null){
            showLoading(true);
            Call<ResponseBody> call = Injector.Api().updateConsultaion(consultationItem);
            call.enqueue(new CallbackWithRetry<ResponseBody>(
                    call,
                    () -> {
                        showNoInternet(true, (v) -> {
                            showNoInternet(false, null);
                            sendCosult();
                        });
                    }
            ) {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Intent i = new Intent();
                        i.putExtra("model",getConsult());
                        setResult(ChatActivity.RESULT_EDITED, i);
                        Toast.makeText(EditConsultActivity.this, R.string.consult_updated, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        showLoading(false);
                        Toast.makeText(EditConsultActivity.this, R.string.consult_not_sent, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean valid() {
        /*if((male.isChecked() || female.isChecked()) && !Validation.isEditTextEmpty(age)
                && !Validation.isEditTextEmpty(consult)){
            try {
                String s = age.getEditableText().toString();
                int gg = Integer.parseInt(s);
                if(gg >= AppController.MIN_AGE && gg <= AppController.MAX_AGE){
                    return true;
                }
            }catch (Exception e){}
        }
        return false;*/
        return (male.isChecked() || female.isChecked()) && !Validation.isEditTextEmpty(age)
                && !Validation.isEditTextEmpty(consult);
    }

    public ConsultationItem getConsult() {
        try {
            String _c = consult.getEditableText().toString();
            int _a = Integer.parseInt(age.getEditableText().toString());
            String _g = female.isChecked() ? ConsultationItem.GENDER_FEMALE : ConsultationItem.GENDER_MALE;
            ConsultationItem consultationItem = new ConsultationItem(_g, getUserId(), _c, _a);
            consultationItem.setId(this.consultationItem.getId());
            consultationItem.setTimeStamp(this.consultationItem.getTimeStamp());
            return  consultationItem ;
        }catch (Exception e){}
        return null;
    }

    @OnClick(R.id.cancel)
    void cancel(){
        finish();
    }
}
