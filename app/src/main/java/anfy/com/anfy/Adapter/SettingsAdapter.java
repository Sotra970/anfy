package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.SettingItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.SettingsSwitchVH;
import anfy.com.anfy.ViewHolder.SettingsVH;

public class SettingsAdapter extends GenericAdapter<SettingItem> {

    private final static int VIEW_TYPE_SWITCH = 0;
    private final static int VIEW_TYPE_REGULAR = 1;

    private Context context;

    public SettingsAdapter(ArrayList<SettingItem> items, GenericItemClickCallback<SettingItem> adapterItemClickCallbacks, Context context) {
        super(items, adapterItemClickCallbacks);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(viewType == VIEW_TYPE_REGULAR ? R.layout.settings_item : R.layout.setting_item_switch, parent);
        return viewType == VIEW_TYPE_REGULAR ? new SettingsVH(v) : new SettingsSwitchVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SettingItem settingItem = getItem(position);
        if(settingItem != null){
            if(position == 0){
                SettingsSwitchVH vh  = (SettingsSwitchVH) holder;
                MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
                vh.image.setImageResource(settingItem.getImageResId());
                vh.title.setText(settingItem.getTitleResId());
                vh.m_switch.setChecked(myPreferenceManager.isNotificationsEnabled());
                vh.m_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    myPreferenceManager.setNotificationsEnabled(isChecked);
                });
            }else{
                SettingsVH vh  = (SettingsVH) holder;
                vh.image.setImageResource(settingItem.getImageResId());
                vh.title.setText(settingItem.getTitleResId());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_SWITCH : VIEW_TYPE_REGULAR;
    }
}
