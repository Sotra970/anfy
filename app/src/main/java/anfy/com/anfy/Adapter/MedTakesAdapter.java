package anfy.com.anfy.Adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.Model.TakeItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.MedTakeViewHolder;

public class MedTakesAdapter extends GenericAdapter<TakeItem> {

    private Activity context;

    public MedTakesAdapter(ArrayList<TakeItem> items, Activity context) {
        super(items, null);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.interval_item, parent);
        return new MedTakeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TakeItem takeItem = getItem(position) ;
        MedTakeViewHolder holder1 = (MedTakeViewHolder) holder;
        holder1.take.setText(context.getString(R.string.take)+(position+1));
        holder1.take_time.setText(TimeUtils.getHours(takeItem.time));
        ((MedTakeViewHolder) holder).take_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.e("MedTakesAdapter","hour " + i);
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        takeItem.time = calendar.getTimeInMillis();
                        String time = TimeUtils.getHours(calendar.getTimeInMillis());
                        holder1.take_time.setText(time);
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false).show();
            }
        });


    }
}
