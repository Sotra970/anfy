package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.AboutUsDocVH;

public class AboutUsDoctorAdapter extends GenericAdapter<DoctorItem> {

    private Context context;

    public AboutUsDoctorAdapter(ArrayList<DoctorItem> items,
                                GenericItemClickCallback<DoctorItem> adapterItemClickCallbacks,
                                Context context) {
        super(items, adapterItemClickCallbacks);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.about_us_doctor, parent);
        return new AboutUsDocVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DoctorItem doctorItem = getItem(position);
        if(doctorItem != null){
            AboutUsDocVH vh = (AboutUsDocVH) holder;
            Glide.with(context).load(Utils.getImageUrl(doctorItem.getImage())).into(vh.image);
            vh.title.setText(doctorItem.getName());
            vh.phone.setText(doctorItem.getPhone());
        }
    }
}
