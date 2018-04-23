package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatTextVH extends RecyclerView.ViewHolder {

    @BindView(R.id.msg)
    public TextView msg;
    @BindView(R.id.date)
    public TextView date;

    public ChatTextVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
