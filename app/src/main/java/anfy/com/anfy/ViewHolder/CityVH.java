package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CityVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;

    public CityVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
