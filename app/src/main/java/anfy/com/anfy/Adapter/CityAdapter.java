package anfy.com.anfy.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.CityItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.CityVH;

public class CityAdapter extends GenericAdapter<CityItem> {
    public CityAdapter(ArrayList<CityItem> items, GenericItemClickCallback<CityItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.city_item, parent);
        return new CityVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CityItem cityItem = getItem(position);
        if(cityItem != null){
            ((CityVH) holder).title.setText(cityItem.getName());
        }
    }
}
