package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.Adapter.AlarmAdapterAdapter;
import anfy.com.anfy.AlarmService.AlarmUtils;
import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.AlarmAdapterCallBack;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developers@appgain.io on 5/8/2018.
 */

public class AlarmDayFragment extends BaseFragment {
    Long time  ;
    public static AlarmDayFragment getInstance(Long time) {
        AlarmDayFragment alarmDayFragment = new AlarmDayFragment();
        alarmDayFragment.time = time ;
        return alarmDayFragment;
    }

    View view ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       if (view == null){
             view = inflater.inflate(R.layout.alarm_day_fragment , container , false ) ;
           ButterKnife.bind(this,view);
           Log.e("AlarmDayFragment" , "time" + time ) ;
           bind_data();
       }
        return view ;
    }


    @BindView(R.id.recycler)
    RecyclerView  recyclerView ;


    AlarmAdapterAdapter alarmAdapterAdapter ;
    void bind_data(){


        Calendar tdcalendar = Calendar.getInstance() ;
        tdcalendar.setTimeInMillis(time);
        String day_month  = tdcalendar.get(Calendar.DAY_OF_MONTH) +"_" +tdcalendar.get(Calendar.MONTH);


        alarmAdapterAdapter = new AlarmAdapterAdapter(null  ,day_month, (MainActivity) getActivity(), new AlarmAdapterCallBack(){
         }) ;
        recyclerView.setAdapter(alarmAdapterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AppController.getExecutorService().submit(() -> {
            Log.e("AlarmDayFragment" , " get data ") ;
            try {
                Calendar calendar = Calendar.getInstance() ;
                calendar.setTimeInMillis(time);
                List<AlarmEntity> list =  DbApi.dao(getContext()).getAlarms(calendar.get(Calendar.DAY_OF_WEEK));
                Log.e("AlarmDayFragment" , "size " + list.size() ) ;
                if (getActivity()!=null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list !=null && !list.isEmpty()){
                            showNoData(false);
                            alarmAdapterAdapter.updateData(getList(list));
                        }else showNoData(true);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    private ArrayList<AlarmEntity> getList(List<AlarmEntity> list) {

        ArrayList<AlarmEntity> alarmEntities = new ArrayList<>();
        for (AlarmEntity entity :  list ){
            if (checkNonContigousDayToAdd(entity)){
                Log.e("AlarmDay" , "added");
                alarmEntities.add(entity) ;
            }
        }
        Log.e("AlarmDay" , "alarmEntities" + alarmEntities.size());
        return  alarmEntities ;
    }


    boolean checkNonContigousDayToAdd(AlarmEntity entity){
        String TAG  = entity.uiModelName + entity.take_number ;

        long start_dif = TimeUnit.MILLISECONDS.toDays(time)  -  TimeUnit.MILLISECONDS.toDays(entity.starting_date) ;
        long end_diff =  TimeUnit.MILLISECONDS.toDays(time) - TimeUnit.MILLISECONDS.toDays(entity.end_data)  ;

        Log.e(TAG , "start : " +  TimeUtils.getFullDate(entity.starting_date)  +"today " +  TimeUtils.getFullDate(time));
        Log.e(TAG, "start : " +  TimeUnit.MILLISECONDS.toDays(entity.starting_date)  +"today " +  TimeUnit.MILLISECONDS.toDays(time));
        Log.e(TAG , "end : " +  TimeUtils.getFullDate(entity.end_data)  +"today " +  TimeUtils.getFullDate(time));
        Log.e(TAG, "end : " +  TimeUnit.MILLISECONDS.toDays(entity.end_data)  +"today " +  TimeUnit.MILLISECONDS.toDays(time));
        Log.e(TAG , "start_dif "+ start_dif +" end_diff " + end_diff );


        if ( end_diff <= 0.0  &&  start_dif >=0.0  && !entity.isContinous ){


            int days = (int) ((entity.days_count*7) - start_dif);


            Log.e(TAG , "daysCount " + entity.days_count +"");
            if (days <=0 ){
                Log.e(TAG , "false");
                return false ;
            }else {
                Log.e(TAG , "true");
                return  true ;
            }



        }else if (entity.isContinous && start_dif >=0.0 ){
                Log.e(TAG , "true");
                return  true ;
        }

        Log.e(TAG , "false");
        return false ;


    }


}
