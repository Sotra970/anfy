package anfy.com.anfy.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.Dialog.SelectWeekDaysActivity;
import anfy.com.anfy.Activity.Dialog.SetDaysCountActivity;
import anfy.com.anfy.AlarmService.AlarmUtils;
import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAlarmActivity extends FragmentSwitchActivity {

    private static final String TAG = "AddAlarmActivity";
    @BindView(R.id.med_name_input)
    EditText med_name_input ;

    @BindView(R.id.starting_day_section_txt)
    TextView starting_day_section_txt ;




    @BindView(R.id.contonious_rd)
    RadioButton contonious_rd ;

    @BindView(R.id.days_count_rd)
    RadioButton days_count_rd ;


    @BindView(R.id.every_day_rd)
    RadioButton every_day_rd ;


    @BindView(R.id.week_days_rd)
    RadioButton week_days_rd ;



    AlarmEntity alarmEntity = new AlarmEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        ButterKnife.bind(this);
        medNameSetup();
        startingDateSetup(Calendar.getInstance());
        rds_setup();
    }

    private void rds_setup() {
        every_day_rd.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                selectEveryDay();
            }
        });

        week_days_rd.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                selectWeekDays();
            }
        });

        contonious_rd.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                selectCountinous();
            }
        });


        days_count_rd.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                selectDaysCount();
            }
        });
    }

    void medNameSetup(){
        med_name_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    alarmEntity.uiModelName = editable.toString() ;
            }
        });

    }

    void startingDateSetup(Calendar calendar){
        String day = TimeUtils.getDayMonthString(calendar.getTimeInMillis());
        starting_day_section_txt.setText(day);
        alarmEntity.starting_date = calendar.getTimeInMillis() ;
    }

    @OnClick(R.id.starting_day_section)
    void startingDayOnClick(){

       Calendar calendar = Calendar.getInstance() ;
        new DatePickerDialog(this, (datePicker, year, month, day) -> {

           calendar.set(year , month , day);
           startingDateSetup(calendar);

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }


    @OnClick(R.id.contonious_section)
    void selectCountinous(){
        Log.e(TAG  , "selectCountinous");
        days_count_rd.setChecked(false);
        contonious_rd.setChecked(true);
        alarmEntity.isContinous=true ;

    }


    //todo  for every update decrease days count if it 0 so delete this alarm from alarms alarmEntity
    @OnClick(R.id.days_count_section)
    void selectDaysCount(){
        Log.e(TAG  , "selectCountinous");
        SetDaysCountActivity.start(getApplicationContext(), new SetDaysCountActivity.DaysCountCallBback() {
            @Override
            public void onConfirm(int count) {
                Log.e(TAG , " count  " + count) ;
                alarmEntity.days_count = count ;
                selectDaysCountRd();
            }

            @Override
            public void onCancel() {
                if ( alarmEntity.days_count < 1 ){
                        selectCountinous();
                }
            }
        });
    }

    void selectDaysCountRd(){
        contonious_rd.setChecked(false);
        days_count_rd.setChecked(true);
        alarmEntity.isContinous=false ;
    }


    @OnClick(R.id.every_day_section)
    void selectEveryDay(){
        Log.e(TAG  , "selectEveryDay") ;
        week_days_rd.setChecked(false);
        every_day_rd.setChecked(true);
        week_days = new ArrayList<>();
        dayly = true ;
    }


    @OnClick(R.id.week_days_section)
    void selectWeekDays(){
        Log.e(TAG  , "week_days_section") ;

        SelectWeekDaysActivity.start(getApplicationContext() , week_days, new SelectWeekDaysActivity.WeekDaysCallBack() {
            @Override
            public void onConfirm(ArrayList<Integer> days) {
                Log.e(TAG  , "week_days_section Days"  + days.toString())  ;
                week_days = days ;
                every_day_rd.setChecked(false);
                week_days_rd.setChecked(true);
                dayly = false ;
            }

            @Override
            public void onCancel() {
                if (week_days != null  && !week_days.isEmpty()){

                }else {
                    selectEveryDay();
                }

            }
        });
    }



    // save properties//
    // days properties
    ArrayList<Integer> week_days = new ArrayList<>() ;
    boolean dayly = true ;

    @OnClick(R.id.save)
    void save(){
        alarmEntity.uiMode  mlId = AppController.getTimeStamp()+"" ;
        if (!dayly){
            for (Integer day  : week_days){
                // set alarm for this day
                AlarmEntity dayAlarmEntity = alarmEntity ;
                dayAlarmEntity.requestCode = (int) AppController.getTimeStamp();
                Calendar calendar = Calendar.getInstance() ;
                calendar.setTimeInMillis(dayAlarmEntity.starting_date);
                // todo get starting time from reminder remind time recycler adapter items
                calendar.set(Calendar.HOUR_OF_DAY , Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE , Calendar.getInstance().get(Calendar.MINUTE));
                calendar.set(Calendar.DAY_OF_WEEK, day);
                //
                dayAlarmEntity.starting_time = calendar.getTimeInMillis() ;
                Bundle bundle = new Bundle();
                bundle.putString("message" , getString(R.string.havind_reminder) +" " + dayAlarmEntity.uiModelName);
                dayAlarmEntity.interval = AlarmUtils.INTERVAL_WEEK ;
                AlarmUtils.setAlarm(getApplicationContext() ,dayAlarmEntity.requestCode ,  dayAlarmEntity.starting_time , dayAlarmEntity.interval ,bundle  );
            }
        }else {
            // set alarm
            alarmEntity.requestCode = (int) AppController.getTimeStamp();
            Calendar calendar = Calendar.getInstance() ;
            calendar.setTimeInMillis(alarmEntity.starting_date);
            // todo get starting time from reminder remind time recycler adapter items
            calendar.set(Calendar.HOUR_OF_DAY , Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE , Calendar.getInstance().get(Calendar.MINUTE));
            //
            alarmEntity.starting_time = calendar.getTimeInMillis() ;
            Bundle bundle = new Bundle();
            bundle.putString("message" , getString(R.string.havind_reminder) +" " + alarmEntity.uiModelName);
            alarmEntity.interval = AlarmUtils.INTERVAL_DAY ;
            alarmEntity.interval = (3*60*1000) ;
            AlarmUtils.setAlarm(getApplicationContext() ,alarmEntity.requestCode ,  alarmEntity.starting_time , alarmEntity.interval ,bundle  );
        }
    }



}
