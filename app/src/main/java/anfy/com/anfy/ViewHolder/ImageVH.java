package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developers@appgain.io on 4/23/2018.
 */

public class ImageVH extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    public ImageView imageView ;

    @BindView(R.id.date)
    public TextView date ;

    public ImageVH(View inflate) {
        super(inflate);
        ButterKnife.bind(this , inflate);
    }
}
