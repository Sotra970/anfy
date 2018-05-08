package anfy.com.anfy.Util;

import android.content.Intent;
import android.net.Uri;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.R;

/**
 * Created by developers@appgain.io on 4/25/2018.
 */

public class ShareUtils {
    public static void shareLink(String link) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
//        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, link);
        AppController.getInstance().startActivity(Intent.createChooser(share, "Share Article"));

    }

    public static void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        AppController.getInstance().startActivity(browserIntent);
    }

    public static void sendEmail(String email ) {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(email);
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        send.putExtra(Intent.EXTRA_SUBJECT, AppController.getInstance().getString(R.string.suggestion));
        AppController.getInstance().startActivity(Intent.createChooser(send, "Send mail..."));
    }
}
