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

    public boolean isContinous ;

    public int days_count ;

    public int enable =1;



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
