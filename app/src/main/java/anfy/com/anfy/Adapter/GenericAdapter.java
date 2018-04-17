package anfy.com.anfy.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.R;

/**
 * Created by Ahmed on 9/8/2017.
 */

public abstract class GenericAdapter<T extends Object> extends RecyclerView.Adapter {

    private ArrayList<T> items;
    private GenericItemClickCallback<T> adapterItemClickCallbacks;

    public GenericAdapter(ArrayList<T> items,
                          GenericItemClickCallback<T> adapterItemClickCallbacks)
    {
        setItems(items);
        this.adapterItemClickCallbacks = adapterItemClickCallbacks;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(adapterItemClickCallbacks != null){
            final T item = getItem(position);
            if(item != null){
                holder.itemView.setOnClickListener(
                        v -> adapterItemClickCallbacks.onItemClicked(item)
                );
            }
        }
    }

    public void addNewItem(T item){
        if(item != null){
            if(items == null){
                items = new ArrayList<>();
            }
            items.add(item);
            notifyItemInserted(items.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    public void removeItems(@Nullable List<T> itemsToRemove){
        if(items != null && !items.isEmpty()){
            if(itemsToRemove != null && !itemsToRemove.isEmpty()){
                items.removeAll(itemsToRemove);
                notifyDataSetChanged();
            }
        }
    }

    public void removeAllWithAnimation(){
        if(items != null && !items.isEmpty()){
            int start = 0;
            int count = items.size();
            items = new ArrayList<>();
            notifyItemRangeRemoved(start, count);
        }
    }

    public void updateData(ArrayList<T> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    protected void setItems(ArrayList<T> is){
        this.items = is;
    }

    @Nullable
    public Integer getItemId(T item){
        if(items == null || items.isEmpty()){
            return null;
        }
        return items.indexOf(item);
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public GenericItemClickCallback<T> getAdapterItemClickCallbacks() {
        return adapterItemClickCallbacks;
    }

    @Nullable
    public T getItem(int position){
        if(items != null && position >= 0 && position < items.size()){
            return items.get(position);
        }
        return null;
    }

    public boolean isDataSetEmpty(){
        return items == null || items.isEmpty();
    }

    protected View inflate(int viewResId, ViewGroup parent){
        return LayoutInflater.from(parent.getContext())
                .inflate(viewResId, parent, false);
    }


}
