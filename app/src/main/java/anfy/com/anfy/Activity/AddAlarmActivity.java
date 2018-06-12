package anfy.com.anfy.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.Dialog.SelectWeekDaysActivity;
import anfy.com.anfy.Activity.Dialog.SetDaysCountActivity;
import anfy.com.anfy.Adapter.MedTakesAdapter;
import anfy.com.anfy.AlarmService.AlarmUtils;
import anfy.com.anfy.AlarmService.RoomLayer.AlarmEntity;
import anfy.com.anfy.AlarmService.RoomLayer.DbApi;
import anfy.com.anfy.AlarmService.RoomLayer.AnfyDao;
import anfy.com.anfy.AlarmService.TimeUtils;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Model.TakeItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Validation;
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

    @BindView(R.id.takes_recycler)
    RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        ButterKnife.bind(this);
        medNameSetup();
        startingDateSetup(Calendar.getInstance());
        rds_setup();
        alarm_setup();
    }

    MedTakesAdapter addMedAdapter  ;
    private void alarm_setup() {
        ArrayList<TakeItem> takeItems = new ArrayList<>() ;
        Calendar calendar = Calendar.getInstance() ;
        calendar.set(Calendar.HOUR_OF_DAY , 8);
        calendar.set(Calendar.MINUTE , 0);
        takeItems.add(new TakeItem(calendar.getTimeInMillis()));
        addMedAdapter = new MedTakesAdapter(takeItems , this) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(addMedAdapter);
    }

    @OnClick(R.id.add_take)
    void addTake(){
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.e("MedTakesAdapter","hour " + i);
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                TakeItem takeItem  = new TakeItem(calendar.getTimeInMillis());
                addMedAdapter.addNewItem(takeItem);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false).show();
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
        alarmEntity.isDaily = 1;
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
                alarmEntity.isDaily = 0;
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

    @OnClick(R.id.save)
    void save(){
        if (Validation.isEditTextEmpty(med_name_input))return;
       AppController.getExecutorService().submit(() -> {
            try {
                alarmEntity.uiModelId = AppController.getTimeStamp()+"" ;
                AnfyDao dao = DbApi.dao(getApplicationContext()) ;
                if (alarmEntity.isDaily==0 && !alarmEntity.isContinous){
                    for (int i =0 ; i<addMedAdapter.getItems().size();i++)
                    {
                        TakeItem takeItem = addMedAdapter.getItems().get(i) ;
                        for (Integer day  : week_days){
                            ArrayList<Long> days_cals = AlarmUtils.getAlarmDaysList(day ,alarmEntity , takeItem);
                            for (int j=0 ; j<days_cals.size();j++){
                                AlarmEntity dayAlarmEntity = new AlarmEntity(alarmEntity) ;
                                dayAlarmEntity.requestCode = (int) AppController.getInstance().getPRefrenceManger().getUniqueID();
                                dayAlarmEntity.day = day ;
                                dayAlarmEntity.starting_date =days_cals.get(j);
                                dayAlarmEntity.end_data = dayAlarmEntity
                                        .starting_date + TimeUnit.DAYS.toMillis(1);
                                Log.e("AddAlarm " , "end date" + TimeUtils.getFullDate(dayAlarmEntity.end_data)) ;
                                Log.e("daysCount" , "days count "  +alarmEntity.days_count + "week days " + week_days.size()) ;
                                dayAlarmEntity.days_count=  1;
                                dayAlarmEntity.starting_time = days_cals.get(j);
                                Log.e("AddAlarm " , "getTimeInMillis date" + TimeUtils.getFullDate(days_cals.get(j))) ;
                                Log.e("AddAlarm " , "starting_time date" + TimeUtils.getFullDate(dayAlarmEntity.starting_time)) ;

                                dayAlarmEntity.take_number = i+1;
                                Bundle bundle = new Bundle();
                                bundle.putString("message" , getString(R.string.havind_reminder) +" " + dayAlarmEntity.uiModelName + "("+getString(R.string.take)    +(i+1)+")");
                                dayAlarmEntity.interval = AlarmUtils.INTERVAL_WEEK ;
                                AlarmUtils.setAlarm(getApplicationContext() ,dayAlarmEntity.requestCode ,  days_cals.get(j) , dayAlarmEntity.interval ,bundle  );
                                dao.insertReminder(dayAlarmEntity);
                            }
                        }
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
                }else if (alarmEntity.isDaily ==0){
                    for (int i =0 ; i<addMedAdapter.getItems().size();i++)
                    {
                        TakeItem takeItem = addMedAdapter.getItems().get(i) ;
                        Calendar takeTime = Calendar.getInstance() ;
                        takeTime.setTimeInMillis(takeItem.time);
                        for (Integer day  : week_days){
                            // set alarm for this day
                            AlarmEntity dayAlarmEntity = new AlarmEntity(alarmEntity) ;
                            dayAlarmEntity.requestCode = (int) AppController.getInstance().getPRefrenceManger().getUniqueID();
                            Calendar calendar = Calendar.getInstance() ;
                            calendar.setTimeInMillis(dayAlarmEntity.starting_date);
                            calendar.set(Calendar.HOUR_OF_DAY , takeTime.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE , takeTime.get(Calendar.MINUTE));
                            calendar.set(Calendar.DAY_OF_WEEK, day);
                            dayAlarmEntity.day = day ;
                            dayAlarmEntity.starting_date = AlarmUtils.checkAlarmDate(Calendar.getInstance().getTimeInMillis() , calendar.getTimeInMillis());


                            if (dayAlarmEntity.isContinous)
                                dayAlarmEntity.end_data = 0 ;
                            else {

                                dayAlarmEntity.end_data = alarmEntity.starting_date + TimeUnit.DAYS.toMillis(dayAlarmEntity.days_count);
                                Log.e("AddAlarm " , "end date" + TimeUtils.getFullDate(dayAlarmEntity.end_data)) ;
                            }


                            if (!dayAlarmEntity.isContinous)
//                       dayAlarmEntity.days_count/=addMedAdapter.getItems().size();
                                Log.e("daysCount" , "days count "  +alarmEntity.days_count + "week days " + week_days.size()) ;
                            dayAlarmEntity.days_count=  new Double(Math.ceil(dayAlarmEntity.days_count/7)).intValue();
                            if (dayAlarmEntity.days_count ==0 ) dayAlarmEntity.days_count = 1 ;
                            Log.e("daysCount" , "days count "  +alarmEntity.days_count + "se alarm day count " +  dayAlarmEntity.days_count + "week days " + week_days.size()) ;
                            //

                            dayAlarmEntity.starting_time = calendar.getTimeInMillis();



                            dayAlarmEntity.take_number = i+1;
                            Bundle bundle = new Bundle();
                            bundle.putString("message" , getString(R.string.havind_reminder) +" " + dayAlarmEntity.uiModelName + "("+getString(R.string.take)    +(i+1)+")");
                            dayAlarmEntity.interval = AlarmUtils.INTERVAL_WEEK ;
                            dayAlarmEntity.starting_time = AlarmUtils.checkAlarmTime(dayAlarmEntity);
                            AlarmUtils.setAlarm(getApplicationContext() ,dayAlarmEntity.requestCode ,  dayAlarmEntity.starting_time , dayAlarmEntity.interval ,bundle  );
                            dao.insertReminder(dayAlarmEntity);
                        }
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
                }else {

                    for (int i =0 ; i<addMedAdapter.getItems().size();i++)
                    {
                        AlarmEntity TakeAlarmEntity = new AlarmEntity(alarmEntity    ) ;

                        TakeItem takeItem = addMedAdapter.getItems().get(i) ;
                        Calendar takeTime = Calendar.getInstance() ;
                        takeTime.setTimeInMillis(takeItem.time);
                        // set alarm
                        TakeAlarmEntity.requestCode = AppController.getInstance().getPRefrenceManger().getUniqueID();
                        Calendar calendar = Calendar.getInstance() ;
                        calendar.setTimeInMillis(TakeAlarmEntity.starting_date);
                        calendar.set(Calendar.HOUR_OF_DAY , takeTime.get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE , takeTime.get(Calendar.MINUTE));
                        TakeAlarmEntity.starting_time = calendar.getTimeInMillis() ;


                        if (TakeAlarmEntity.isContinous)
                            TakeAlarmEntity.end_data = 0 ;
                        else {
                            TakeAlarmEntity.end_data = alarmEntity.starting_date + TimeUnit.DAYS.toMillis(alarmEntity.days_count);
//                       TakeAlarmEntity.days_count/= addMedAdapter.getItems().size() ;
                        }
                        TakeAlarmEntity.take_number = i+1;
                        Bundle bundle = new Bundle();
                        bundle.putString("message" , getString(R.string.havind_reminder) +" " + TakeAlarmEntity.uiModelName + "("+getString(R.string.take)+(i+1)+")");
                        TakeAlarmEntity.interval = AlarmUtils.INTERVAL_DAY ;
                        TakeAlarmEntity.starting_time = AlarmUtils.checkAlarmTime(TakeAlarmEntity);
                        AlarmUtils.setAlarm(getApplicationContext() ,TakeAlarmEntity.requestCode ,  TakeAlarmEntity.starting_time , TakeAlarmEntity.interval ,bundle  );
                        dao.insertReminder(TakeAlarmEntity);
                    }

                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
       });


    }


    @OnClick(R.id.cancel)
    void cancel(){
        finish();
    }




}
