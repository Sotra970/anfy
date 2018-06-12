package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import anfy.com.anfy.Activity.AddAlarmActivity;
import anfy.com.anfy.Adapter.AlarmAdapterAdapter;
import anfy.com.anfy.Adapter.ViewPager.NoTitlePagerAdapter;
import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.AlarmAdapterCallBack;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.DayTabTitleHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmsFragment extends TitledFragment {

    public static final int ADD_ALARM_REQUEST_CODE = 13;

    public static AlarmsFragment getInstance() {
        return new AlarmsFragment();
    }

    public  static HashMap<String ,Integer> nonContinousdaysHashMap = new HashMap<>() ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.alarm_fragment , container , false) ;
        ButterKnife.bind(this , view) ;
        nonContinousdaysHashMap = new HashMap<>() ;
        bindData();
        return view ;
    }

    @BindView(R.id.selected_day)
    TextView selected_day ;

    void slectedDaySetup(long Time){
        selected_day.setText(TimeUtils.getDayMonthString(Time));
    }


    ArrayList<DayTabTitleHolder> dayTabTitleHolders = new ArrayList<>() ;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>() ;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout ;
    @BindView(R.id.pager)
    ViewPager viewPager  ;

    void bindData(){

                NoTitlePagerAdapter pagerAdapter = new NoTitlePagerAdapter(getChildFragmentManager()) ;
                for (Long time : getMonthDays()){
                    final boolean selected;
                    DayTabTitleHolder dayTabTitleHolder = new DayTabTitleHolder(time , getActivity()) ;
                    TabLayout.Tab tab = tabLayout.newTab() ;
                    if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == dayTabTitleHolder.day){
                        dayTabTitleHolder.select();
                        selected = true ;
                    }else {
                        dayTabTitleHolder.unSelect();
                        selected = false;
                    }
                    tabLayout.addTab(tab , selected);

                    dayTabTitleHolders.add(dayTabTitleHolder) ;

                    Fragment fragment = AlarmDayFragment.getInstance(time) ;
                    fragmentArrayList.add(fragment);
                    pagerAdapter.addFragment(fragment);
                }
                viewPager.setAdapter(pagerAdapter);
                tabLayout.setupWithViewPager(viewPager);




        tabLayout.post(() -> {
            int selected = 0 ;
            for (int i= 0 ; i<dayTabTitleHolders.size() ; i++){
                View view = dayTabTitleHolders.get(i).getView() ;
                tabLayout.getTabAt(i).setCustomView(view) ;
                if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == dayTabTitleHolders.get(i).day){
                    tabLayout.getTabAt(i).select();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                DayTabTitleHolder dayTabTitleHolder = dayTabTitleHolders.get(tab.getPosition());
                dayTabTitleHolder.select();
                slectedDaySetup(dayTabTitleHolder.child);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                dayTabTitleHolders.get(tab.getPosition()).unSelect();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    ArrayList<Long> getMonthDays(){
        ArrayList<Long> days  = new ArrayList<>() ;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < maxDay; i++) {
            Log.e("getMonthDays" , TimeUtils.getFullDate(cal.getTimeInMillis()));
            days.add(cal.getTimeInMillis());
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
        }
        return days ;
    }

    @OnClick(R.id.add_alarm)
    void add_alarm(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new AlertDialog.Builder(getActivity()).setMessage("لا يدعم نظام تشغيل اوريو بعد")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        }else
        getActivity().startActivityForResult(new Intent(getActivity() , AddAlarmActivity.class) ,AlarmsFragment.ADD_ALARM_REQUEST_CODE );
    }


    @Override
    public int getTitleResId() {
        return R.string.nav_alarm;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("alarm", "activity: onActivityResult " + requestCode + "  " + resultCode );
    }
}
