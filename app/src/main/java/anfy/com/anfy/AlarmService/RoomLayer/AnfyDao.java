package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developers@appgain.io on 4/15/2018.
 */

@Dao
public interface AnfyDao {



    @Query(
            "SELECT * from AlarmEntity "
    )
    List<AlarmEntity> getRemindersList();
    @Query(
            "SELECT AlarmEntity.* "+
                    " FROM AlarmEntity Where AlarmEntity.requestCode = :requestCode "
    )
    List<AlarmEntity> getReminder(int requestCode);

    @Query(
            "SELECT AlarmEntity.* "+
                    " FROM AlarmEntity" +
                    " Where" +
                    " ( AlarmEntity.isContinous = 1 AND  AlarmEntity.isDaily=1 )" +
                    " OR  " +
                    "( AlarmEntity.isContinous = 1 AND  AlarmEntity.day = :day  ) " +
                    " OR " +
                    "( AlarmEntity.isContinous = 0 AND  AlarmEntity.isDaily=1 )" +// in the case check for this alarm  display_times-days_times and siplay it upon the list
                    " OR  " +
                    "( AlarmEntity.isContinous = 0 AND  AlarmEntity.day = :day ) "
    )
    List<AlarmEntity> getAlarms(int day);



    @Update
    void updateAlarm(AlarmEntity alarmEntity);


    @Query( " DELETE FROM AlarmEntity WHERE requestCode = :requestCode")
    void deleteAlarm(int requestCode);

    @Delete
    void deleteAlarm(AlarmEntity alarmEntity);

    @Insert
    void insertReminder(AlarmEntity alarmEntity);
}
