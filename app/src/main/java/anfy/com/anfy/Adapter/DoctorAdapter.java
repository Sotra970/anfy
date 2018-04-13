package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Interface.DoctorCallbacks;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.DoctorVH;

public class DoctorAdapter extends GenericAdapter<DoctorItem> {

    private DoctorCallbacks doctorCallbacks;
    private Context context;

    public DoctorAdapter(ArrayList<DoctorItem> items, Context context, DoctorCallbacks doctorCallbacks) {
        super(items, null);
        this.doctorCallbacks = doctorCallbacks;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.doctor_item, parent);
        return new DoctorVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DoctorItem doctorItem = getItem(position);
        if(doctorItem != null){
            DoctorVH vh = (DoctorVH) holder;
            if(doctorCallbacks != null){
                vh.itemView.setOnClickListener(v -> {
                    doctorCallbacks.onDoctorItemClicked(doctorItem);
                });
                vh.call.setOnClickListener(v ->{
                    doctorCallbacks.onDoctorCall(doctorItem);
                });
            }
            vh.title.setText(doctorItem.getName());
            Glide.with(context).load(doctorItem.getImage()).into(vh.image);
        }
    }
}
