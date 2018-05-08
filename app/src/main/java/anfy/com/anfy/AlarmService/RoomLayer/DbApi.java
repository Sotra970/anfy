package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

/**
 * Created by sotra on 4/15/2018.
 */

public class DbApi {


    public  static  IkhairDatabase db(Context context){

        IkhairDatabase db = Room.databaseBuilder(context, IkhairDatabase.class, "ikhair-db").fallbackToDestructiveMigration().build();
        // for migration
        return db ;
    }

    public  static IkhairDao dao(Context context){
        return db(context).IkhairDao() ;
    }


}
