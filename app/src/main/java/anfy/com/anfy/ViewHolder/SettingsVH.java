package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsVH extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.image)
    public ImageView image;

    public SettingsVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
