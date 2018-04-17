package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.DrawerItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.DrawerItemVH;

public class DrawerAdapter extends GenericAdapter<DrawerItem> {

    private DrawerItem selectedItem;
    private Context context;

    public DrawerAdapter(ArrayList<DrawerItem> items, Context context, GenericItemClickCallback<DrawerItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.nav_drawer_item, parent);
        return new DrawerItemVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DrawerItem drawerItem = getItem(position);
        if(drawerItem != null){
            DrawerItemVH vh = (DrawerItemVH) holder;
            Glide.with(context).load(drawerItem.getImageResItem()).into(vh.image);
            vh.title.setText(drawerItem.getTextResItem());
            vh.active.setVisibility(drawerItem == selectedItem ? View.VISIBLE : View.GONE);
        }
    }

    public void selectItem(DrawerItem drawerItem){
        int prev = -1;
        if(selectedItem != null){
            prev = getItems().indexOf(selectedItem);
        }
        int pos = getItems().indexOf(drawerItem);
        selectedItem = drawerItem;
        if(prev != -1){
            notifyItemChanged(prev);
        }
        notifyItemChanged(pos);
    }

    public void selectItemByStringId(int stringId){
        ArrayList<DrawerItem> drawerItems = getItems();
        if(drawerItems != null && !drawerItems.isEmpty()){
            DrawerItem toSelect = null;
            int i = 0;
            for(DrawerItem drawerItem : drawerItems){
                if(drawerItem.getTextResItem() == stringId){
                    toSelect = drawerItem;
                    break;
                }
                i++;
            }
            if(toSelect != null){
                selectItem(toSelect);
            }
        }
    }
}
