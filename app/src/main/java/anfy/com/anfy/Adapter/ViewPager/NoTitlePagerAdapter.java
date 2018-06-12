package anfy.com.anfy.Adapter.ViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.R;

public class NoTitlePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public NoTitlePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment ) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
