package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorInfoActivity extends BaseActivity {

    private static DoctorItem doctorItem;

    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.title)
    TextView title;

    public static void setDoctorItem(DoctorItem doctorItem) {
        DoctorInfoActivity.doctorItem = doctorItem;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        ButterKnife.bind(this);
        init();
        load();
    }

    private void init() {
        title.setText(R.string.doctor_details);
    }

    private void load() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DoctorInfoActivity.doctorItem = null;
    }

    @OnClick(R.id.close)
    void close(){
        finish();
    }
}
