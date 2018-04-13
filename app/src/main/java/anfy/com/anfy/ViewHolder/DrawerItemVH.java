package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerItemVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.active)
    public View active;
    @BindView(R.id.count)
    public TextView count;
    @BindView(R.id.count_layout)
    public View countLayout;

    public DrawerItemVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
