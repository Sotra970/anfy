package anfy.com.anfy.Util;

import java.sql.Statement;
import java.util.ArrayList;

import anfy.com.anfy.Model.StaticDataItem;

public class StaticData {

    private static ArrayList<StaticDataItem> dataItems;

    public static void setDataItems(ArrayList<StaticDataItem> dataItems) {
        StaticData.dataItems = dataItems;
    }

    public static StaticDataItem getData(int id){
        if(dataItems != null && !dataItems.isEmpty()){
            for(StaticDataItem staticDataItem : dataItems){
                if(staticDataItem.getId() == id){
                    return staticDataItem;
                }
            }
        }
        return null;
    }
}
