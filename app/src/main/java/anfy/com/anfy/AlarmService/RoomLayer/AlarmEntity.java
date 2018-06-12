package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by developers@appgain.io on 4/26/2018.
 */

@Entity
public class AlarmEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    int id ;
    public String uiModelId;
    public String uiModelName ;
    public long starting_date;
    public long starting_time;
    public long interval;
    public int requestCode;

    public boolean isContinous = true ;

    public int days_count ;
    public int isDaily =1 ;
    public int day ;
    public int take_number ;

    public int enable =1;
    public long end_data;

    public AlarmEntity(AlarmEntity alarmEntity) {
        this.id = alarmEntity.id;
        this.uiModelId = alarmEntity.uiModelId;
        this.uiModelName = alarmEntity.uiModelName;
        this.starting_date = alarmEntity.starting_date;
        this.starting_time = alarmEntity.starting_time;
        this.interval = alarmEntity.interval;
        this.requestCode = alarmEntity.requestCode;
        this.isContinous = alarmEntity.isContinous;
        this.days_count = alarmEntity.days_count;
        this.isDaily = alarmEntity.isDaily;
        this.day = alarmEntity.day;
        this.take_number = alarmEntity.take_number;
        this.enable = alarmEntity.enable;
        this.end_data = alarmEntity.end_data;
    }

    public AlarmEntity() {
    }



    @Override
    public String toString() {
        return "AlarmEntity{" +
                "uiModelId='" + uiModelId + '\'' +
                ", projectname='" + uiModelName + '\'' +
                ", starting_date=" + starting_date +
                ", starting_time=" + starting_time +
                ", interval=" + interval +
                ", requestCode=" + requestCode +
                '}';
    }
}
