package anfy.com.anfy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsSwitchVH extends RecyclerView.ViewHolder {

    @BindView(R.id.m_switch)
    public SwitchCompat m_switch;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.image)
    public ImageView image;

    public SettingsSwitchVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
