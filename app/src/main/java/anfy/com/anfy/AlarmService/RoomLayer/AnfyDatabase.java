package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;



/**
 * Created by sotra on 4/15/2018.
 */

@Database(entities = {
        AlarmEntity.class
        },
        version = 2)
public abstract class AnfyDatabase extends RoomDatabase {
    public abstract AnfyDao IkhairDao();
}
