package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.CountryVH;

public class CountryAdapter extends GenericAdapter<CountryItem> {

    private Context context;

    public CountryAdapter(ArrayList<CountryItem> items,
                          GenericItemClickCallback<CountryItem> adapterItemClickCallbacks,
                          Context context) {
        super(items, adapterItemClickCallbacks);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.country_item, parent);
        return new CountryVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CountryItem countryItem = getItem(position);
        if(countryItem != null){
            CountryVH countryVH = (CountryVH) holder;
            countryVH.title.setText(countryItem.getName());
            Glide
                    .with(context)
                    .load(Utils.getImageUrl(countryItem.getIcon()))
                    .into(countryVH.image);
        }
    }
}
