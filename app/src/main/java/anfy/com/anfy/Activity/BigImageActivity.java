package anfy.com.anfy.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jsibbold.zoomage.ZoomageView;



import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageActivity extends AppCompatActivity{


    @BindView(R.id.myZoomageView)
    ZoomageView mBigImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        if (getIntent().getExtras()==null){
            finish();
        }
        if (getIntent().getExtras().get("url") == null){
            finish();
        }

        Glide.with(this)
                .load(getIntent().getExtras().getString("url"))
                .apply(new RequestOptions().fitCenter())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mBigImage);
    }
}
