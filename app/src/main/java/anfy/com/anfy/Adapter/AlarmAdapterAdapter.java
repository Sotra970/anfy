package anfy.com.anfy.Adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Handler;

import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.AlarmAdapterCallBack;
import anfy.com.anfy.Model.TakeItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.MedTakeViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmAdapterAdapter extends GenericAdapter<AlarmEntity> {
    private final String today;
    AlarmAdapterCallBack alarmAdapterCallBack ;
    private MainActivity main;

    public AlarmAdapterAdapter(ArrayList<AlarmEntity> items ,String today,MainActivity main ,  AlarmAdapterCallBack alarmAdapterCallBack) {
        super(items, null);
        this.alarmAdapterCallBack =alarmAdapterCallBack ;
        this.today = today ;
        this.main = main ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.alarm_item, parent);
        return new DayAlermItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolderholder, int position) {
        super.onBindViewHolder(viewHolderholder, position);
        AlarmEntity alarmEntity = getItem(position) ;
        DayAlermItemViewHolder holder = (DayAlermItemViewHolder) viewHolderholder;

        holder.switcher.setChecked(alarmEntity.enable == 1 ? true : false);
        holder.take_time.setText(TimeUtils.getHours(alarmEntity.starting_time));
        holder.name.setText(alarmEntity.uiModelName+"");
        holder.take_number.setText(holder.take_number.getContext().getString(R.string.take)+" "+alarmEntity.take_number+"");
        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alarmEntity.enable = b ? 1 : 0 ;
                AppController.getExecutorService().submit(new Runnable() {
                    @Override
                    public void run() {
                        AppController.getDb().IkhairDao().updateAlarm(alarmEntity);
                    }
                }) ;
            }
        });

        Log.e("isTake"  , alarmEntity.uiModelName);
        int taken = AppController.getInstance().getPRefrenceManger().isTake(today , alarmEntity.requestCode+"");
        int accent = ContextCompat.getColor(holder.taken_img.getContext() , R.color.colorAccent) ;
        int grey = ContextCompat.getColor(holder.taken_img.getContext() , R.color.grey_400) ;

        Log.e("alarm" , taken +"") ;
        if (taken==1){
            holder.taken_img.setVisibility(View.VISIBLE);
            holder.taken_img.setColorFilter(accent , PorterDuff.Mode.SRC_ATOP);
            holder.pills.setColorFilter(accent , PorterDuff.Mode.SRC_ATOP);
            holder.take_time.setTextColor(accent);
            holder.take_number.setTextColor(accent);
        }else if (taken==0){
            holder.taken_img.setVisibility(View.VISIBLE);
            holder.taken_img.setColorFilter(  grey, PorterDuff.Mode.SRC_ATOP);
            holder.pills.setColorFilter(  grey, PorterDuff.Mode.SRC_ATOP);
            holder.take_time.setTextColor(grey);
            holder.take_number.setTextColor(grey);
        }else {
            holder.taken_img.setVisibility(View.GONE);
        }

        holder.taken_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().getPRefrenceManger().takeMed(today , alarmEntity.requestCode+"");
                notifyItemChanged(position);
            }
        });


        holder.missd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AppController.getInstance().getPRefrenceManger().missMed(today , alarmEntity.requestCode+"");
                    notifyItemChanged(position);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getExecutorService().submit(new Runnable() {
                    @Override
                    public void run() {
                        AppController.getDb().IkhairDao().deleteAlarm(alarmEntity.requestCode);
                        main.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        main.updaetAlarm();
                                    }
                                },300);
                            }
                        });
                    }
                }) ;
            }
        });


    }

    public class DayAlermItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.switcher)
        public SwitchCompat switcher;

        @BindView(R.id.take_time)
        public TextView take_time;

        @BindView(R.id.name)
        public TextView name;

        @BindView(R.id.take_number)
        public TextView take_number;

        @BindView(R.id.taken_img)
        ImageView taken_img ;

        @BindView(R.id.pills)
        ImageView pills ;


        @BindView(R.id.done)
        View taken_med ;


        @BindView(R.id.missd)
        View missd;
        @BindView(R.id.delete)
        View delete ;

        public DayAlermItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
    }
}
