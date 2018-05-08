package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by developers@appgain.io on 4/15/2018.
 */

@Dao
public interface IkhairDao {



    @Query(
            "SELECT * from AlarmEntity "
    )
    List<AlarmEntity> getRemindersList();
    @Query(
            "SELECT AlarmEntity.* "+
                    " FROM AlarmEntity Where AlarmEntity.requestCode = :requestCode "
    )
    List<AlarmEntity> getReminder(int requestCode);

    @Update
    void updateAlarm(AlarmEntity alarmEntity);





}
