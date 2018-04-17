package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.image)
    public ImageView image;

    public CountryVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
