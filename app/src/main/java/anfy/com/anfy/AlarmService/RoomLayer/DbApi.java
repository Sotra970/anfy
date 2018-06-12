package anfy.com.anfy.AlarmService.RoomLayer;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by sotra on 4/15/2018.
 */

public class DbApi {


    public  static AnfyDatabase db(Context context){

        AnfyDatabase db = Room.databaseBuilder(context, AnfyDatabase.class, "anfy-db").fallbackToDestructiveMigration().build();
        // for migration
        return db ;
    }

    public  static AnfyDao dao(Context context){
        return db(context).IkhairDao() ;
    }


}
