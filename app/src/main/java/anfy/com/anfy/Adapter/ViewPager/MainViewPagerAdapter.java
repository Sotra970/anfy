package anfy.com.anfy.Adapter.ViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.R;

public class MainViewPagerAdapter extends BaseFragmentPagerAdapter {

    public MainViewPagerAdapter(ArrayList<Fragment> fragments, @NonNull FragmentManager fragmentManager) {
        super(fragments, fragmentManager);
    }

    public View getTabView(int pos, Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout_item_black, null);
        int titleResId = -1, imageResId = -1;
        switch (pos){
            case 0:
                titleResId = R.string.home;
                imageResId = R.drawable.ic_home_black_36dp;
                break;
            case 1:
                titleResId = R.string.about_us;
                imageResId = R.drawable.information_menu;
                break;
            case 2:
                titleResId = R.string.consultations;
                imageResId = R.drawable.stethoscope_active;
                break;
            case 3:
                titleResId = R.string.alarm;
                imageResId = R.drawable.ic_access_alarms_black_36dp;
                break;
        }
        if(titleResId != -1 && imageResId != -1){
            try {
                ((TextView) view.findViewById(R.id.title)).setText(titleResId);
                ((ImageView) view.findViewById(R.id.image)).setImageResource(imageResId);
            }catch (Exception e){
                Log.e("getTabView()", e.getMessage());
            }
        }
        return view;
    }
}
