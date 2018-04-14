package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorInfoActivity extends BaseActivity {

    private static DoctorItem doctorItem;

    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.title)
    TextView toolbarTitle;
    @BindView(R.id.dr_title)
    TextView drTitle;
    @BindView(R.id.country)
    TextView country;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.degrees)
    TextView degrees;

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
        toolbarTitle.setText(R.string.doctor_details);
    }

    private void load() {
        if(doctorItem != null){
            Glide.with(this).load(Utils.getImageUrl(doctorItem.getImage())).into(image);
            drTitle.setText(doctorItem.getName());
            country.setText(doctorItem.getCountry());
            city.setText(doctorItem.getCity());
            phone.setText(doctorItem.getPhone());
            List<String> deg = doctorItem.getCertificates();
            StringBuilder builder = new StringBuilder();
            for(String s : deg){
                builder.append(" - ")
                        .append(s)
                        .append('\n')
                        .append('\n');
            }
            degrees.setText(builder.toString());
        }
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

    @OnClick(R.id.call)
    void call(){
        if(doctorItem != null){
            Utils.callPhone(doctorItem.getPhone(), this);
        }
    }
}
