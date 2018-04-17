package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TimeUtils;
import anfy.com.anfy.ViewHolder.ConsultationVH;

public class ConsultationsAdapter extends GenericAdapter<ConsultationItem> {

    private Context context;

    public ConsultationsAdapter(ArrayList<ConsultationItem> items,
                                GenericItemClickCallback<ConsultationItem> adapterItemClickCallbacks,
                                Context context) {
        super(items, adapterItemClickCallbacks);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.consultant_item, parent);
        return new ConsultationVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ConsultationItem item = getItem(position);
        if(item != null){
            ConsultationVH vh = (ConsultationVH) holder;
            vh.title.setText(item.getDetails());
            vh.id.setText(item.getId() + "");
            vh.date.setText(TimeUtils.getFromWhen(item.getTimeStamp(), context));
        }
    }
}
