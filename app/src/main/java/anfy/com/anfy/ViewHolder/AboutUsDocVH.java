package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsDocVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.phone)
    public TextView phone;
    @BindView(R.id.image)
    public CircleImageView image;

    public AboutUsDocVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
