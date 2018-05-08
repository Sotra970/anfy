package anfy.com.anfy.Activity.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectWeekDaysActivity extends BaseActivityDialog {

    static  WeekDaysCallBack weekDaysCallBack;
    public  static  ArrayList<Integer> days = new ArrayList<>() ;
    public static void  start(Context context , ArrayList<Integer> days , WeekDaysCallBack daysCountCallBback ){
        SelectWeekDaysActivity.days = days ;
        Intent intent = new Intent(context , SelectWeekDaysActivity.class) ;
        SelectWeekDaysActivity.weekDaysCallBack = daysCountCallBback ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_week_days);
        ButterKnife.bind(this);
        showCloseButton(false);
        setDialogTitle(R.string.specfic_week_days);

        if (days.contains(new Integer(Calendar.SATURDAY))){
            saturday_cb.setChecked(true);
        }else {
            saturday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.SUNDAY))){
            sunday_cb.setChecked(true);
        }else {
            sunday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.MONDAY))){
            monday_cb.setChecked(true);
        }else {
            monday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.TUESDAY))){
            tuesday_cb.setChecked(true);
        }else {
            tuesday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.WEDNESDAY))){
            wednesday_cb.setChecked(true);
        }else {
            wednesday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.THURSDAY))){
            thursday_cb.setChecked(true);
        }else {
            thursday_cb.setChecked(false);
        }


        if (days.contains(new Integer(Calendar.FRIDAY))){
            friday_cb.setChecked(true);
        }else {
            friday_cb.setChecked(false);
        }


        friday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            friday_section_click();
        });



        saturday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            saturday_section_click();
        });



        sunday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            sunday_section_click();
        });



        monday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            monday_section_click();
        });



        tuesday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            tuesday_section_click();
        });



        wednesday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            wednesday_section_click();
        });
        thursday_cb.setOnCheckedChangeListener((compoundButton, b) -> {
            thursday_section_click();
        });



//        conTxt.setText(R.string.send);
//        cnclTxt.setText(R.string.cancel);
    }



    @BindView(R.id.saturday_cb)
    CheckBox saturday_cb ;

    @OnClick(R.id.saturday_section)
    void saturday_section_click(){
        if (!saturday_cb.isChecked()){
            days.remove(new Integer(Calendar.SATURDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.SATURDAY)))
            days.add(new Integer(Calendar.SATURDAY)) ;
        }
    }



    @BindView(R.id.sunday_cb)
    CheckBox sunday_cb ;

    @OnClick(R.id.sunday_section)
    void sunday_section_click(){

        if (!sunday_cb.isChecked()){
            days.remove(new Integer(Calendar.SUNDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.SUNDAY)))
            days.add(new Integer(Calendar.SUNDAY)) ;
        }
    }



    @OnClick(R.id.cancel)
    void cancel(){
       weekDaysCallBack.onCancel();
        finish();
    }



    @BindView(R.id.monday_cb)
    CheckBox monday_cb ;

    @OnClick(R.id.monday_section)
    void monday_section_click(){

        if (!monday_cb.isChecked()){
            days.remove(new Integer(Calendar.MONDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.MONDAY)))
                days.add(new Integer(Calendar.MONDAY)) ;
        }
    }



    @BindView(R.id.tuesday_cb)
    CheckBox tuesday_cb ;

    @OnClick(R.id.tuesday_section)
    void tuesday_section_click(){

        if (!tuesday_cb.isChecked()){
            days.remove(new Integer(Calendar.TUESDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.TUESDAY)))
                days.add(new Integer(Calendar.TUESDAY)) ;
        }
    }



    @BindView(R.id.wednesday_cb)
    CheckBox wednesday_cb ;

    @OnClick(R.id.wednesday_section)
    void wednesday_section_click(){

        if (!wednesday_cb.isChecked()){
            days.remove(new Integer(Calendar.WEDNESDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.WEDNESDAY)))
                days.add(new Integer(Calendar.WEDNESDAY)) ;
        }
    }



    @BindView(R.id.thursday_cb)
    CheckBox thursday_cb ;

    @OnClick(R.id.thursday_section)
    void thursday_section_click(){

        if (!thursday_cb.isChecked()){
            days.remove(new Integer(Calendar.THURSDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.THURSDAY)))
                days.add(new Integer(Calendar.THURSDAY)) ;
        }
    }


    @BindView(R.id.friday_cb)
    CheckBox friday_cb ;

    @OnClick(R.id.friday_section)
    void friday_section_click(){
        if (!friday_cb.isChecked()){
            days.remove(new Integer(Calendar.FRIDAY)) ;
        }else {
            if (!days.contains(new Integer(Calendar.FRIDAY)))
                days.add(new Integer(Calendar.FRIDAY)) ;
        }
    }





    @OnClick(R.id.confirm)
    void confirm(){
       if (days.isEmpty() || days.size() == 7){
           weekDaysCallBack.onCancel();
           finish();
       }else {
           weekDaysCallBack.onConfirm(days);
           finish();
       }
    }




    @Override
    public void onBackPressed() {
        weekDaysCallBack.onCancel();
        super.onBackPressed();
    }

    public  static  interface  WeekDaysCallBack extends Serializable {
        void onConfirm(ArrayList<Integer> days);
        void onCancel();
    }
}
