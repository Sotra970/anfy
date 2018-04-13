package anfy.com.anfy.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {

    public static void callPhone(String phone, Context context){
        if(phone != null && !phone.isEmpty()){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        }
    }
}
