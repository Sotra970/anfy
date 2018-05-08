package anfy.com.anfy.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import anfy.com.anfy.App.AppController;

public class Utils {

    public static void callPhone(String phone, Context context){
        if(phone != null && !phone.isEmpty()){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        }
    }

    public static String getImageUrl(String name){
        return AppController.IMAGE_URL + name;
    }

    public static Spanned getHtmmlText(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(html);
        }

    }
}
