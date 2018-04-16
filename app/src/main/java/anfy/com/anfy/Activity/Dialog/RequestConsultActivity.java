package anfy.com.anfy.Activity.Dialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RequestConsultActivity extends BaseActivityDialog{

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_request_consult);
        ButterKnife.bind(this);
        showCloseButton(true);
        setDialogTitle(R.string.request_consult);
    }

    @OnClick(R.id.pos)
    void createConsult(){
        if(valid()){
            sendCosult();
        }
    }

    private void sendCosult() {
        ConsultationItem consultationItem = getConsult();
        if(consultationItem != null){
            showLoading(true);
            Call<ResponseBody> call = Injector.Api().sendCosult(consultationItem);
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
                        Toast.makeText(RequestConsultActivity.this, R.string.consult_sent, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        showLoading(false);
                        Toast.makeText(RequestConsultActivity.this, R.string.consult_not_sent, Toast.LENGTH_SHORT).show();
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
            return new ConsultationItem(_g, getUserId(), _c, _a);
        }catch (Exception e){}
        return null;
    }
}
