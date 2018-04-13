package anfy.com.anfy.Util;

import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import anfy.com.anfy.Activity.PrivacyAgreementActivity;
import anfy.com.anfy.R;

public class TextViewUtils {

    public static void setMultiColorTextView(String s1, String s2, int color1, int color2,
                                             ClickableSpan clickableSpan, TextView textView){
        String total = s1 + " " + s2;
        int  i = s1.length() ;
        Spannable wordtoSpan = new SpannableString(total);
        wordtoSpan.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(textView.getResources(), color1, null)), 0, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(clickableSpan, i, total.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTextColor(ResourcesCompat.getColor(textView.getResources(), color2, null));
        textView.setText(wordtoSpan);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
