package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.call)
    public View call;
    @BindView(R.id.image)
    public CircleImageView image;

    public DoctorVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
