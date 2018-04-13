package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationVH extends RecyclerView.ViewHolder {

    @BindView(R.id.msg)
    public TextView msg;
    @BindView(R.id.delete)
    public View delete;
    @BindView(R.id.time)
    public TextView date;
    @BindView(R.id.bullet)
    public ImageView bullet;
    @BindView(R.id.time_icon)
    public ImageView dateIcon;
    @BindView(R.id.delete_icon)
    public ImageView deleteIcon;


    public NotificationVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
