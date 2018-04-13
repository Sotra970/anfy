package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsultationVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.consultant_id)
    public TextView id;
    @BindView(R.id.date)
    public TextView date;

    public ConsultationVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
