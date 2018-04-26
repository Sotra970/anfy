package anfy.com.anfy.Activity.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SuggestionActivity extends BaseActivityDialog {

    @BindView(R.id.confirm_text)
    TextView conTxt;
    @BindView(R.id.cancel_text)
    TextView cnclTxt;
    @BindView(R.id.msg)
    EditText msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_msg);
        ButterKnife.bind(this);
        showCloseButton(true);
        setDialogTitle(R.string.send_suggestion);
        conTxt.setText(R.string.send);
        cnclTxt.setText(R.string.cancel);
    }

    @OnClick(R.id.confirm)
    void confirm(){
        if(!Validation.isEditTextEmpty(msg)){
            showLoading(true);
            Call<ResponseBody> call =
                    Injector.Api().addSuggestion(getUserId(), msg.getEditableText().toString());
            call.enqueue(new CallbackWithRetry<ResponseBody>(
                    call,
                    () -> {
                        showNoInternet(true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showNoInternet(false, null);
                                confirm();
                            }
                        });
                    }
            ) {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(SuggestionActivity.this, R.string.sugg_submit, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    @OnClick(R.id.cancel)
    void cancel(){
        finish();
    }
}
