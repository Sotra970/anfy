package anfy.com.anfy.Model;

import android.widget.TextView;

public class SettingItem {

    private int imageResId;
    private int titleResId;

    public SettingItem(int imageResId, int titleResId) {
        this.imageResId = imageResId;
        this.titleResId = titleResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getTitleResId() {
        return titleResId;
    }
}
