package anfy.com.anfy.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.SettingItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.SettingsVH;

public class SettingsAdapter extends GenericAdapter<SettingItem> {

    public SettingsAdapter(ArrayList<SettingItem> items, GenericItemClickCallback<SettingItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.settings_item, parent);
        return new SettingsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SettingItem settingItem = getItem(position);
        if(settingItem != null){
            SettingsVH vh  = (SettingsVH) holder;
            vh.image.setImageResource(settingItem.getImageResId());
            vh.title.setText(settingItem.getTitleResId());
        }
    }
}
