package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by developers@appgain.io on 5/8/2018.
 */


public class MedTakeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.take)
    public TextView take;
    @BindView(R.id.take_time)
    public TextView take_time;



    public MedTakeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
