package anfy.com.anfy.Adapter.ViewPager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Ahmed on 10/1/2017.
 */

public class BaseFragmentPagerAdapter extends PagerAdapter {

    private ArrayList<Fragment> fragments;

    private FragmentManager fragmentManager;

    @Nullable
    private FragmentTransaction fragmentTransaction;

    public BaseFragmentPagerAdapter(ArrayList<Fragment> fragments,
                                    @NonNull FragmentManager fragmentManager)
    {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public int getCount() {
        if(fragments == null || fragments.isEmpty())
            return 0;
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, @Nullable Object object) {
        if(object != null) {
            return view == ((Fragment) object).getView();
        }
        return false;
    }

    @Nullable
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if(fragments != null && !fragments.isEmpty()
                && position < fragments.size())
        {
            if (fragmentTransaction == null) {
                fragmentTransaction = fragmentManager.beginTransaction();
            }

            Fragment f = fragments.get(position);

            fragmentTransaction.add(
                    container.getId(),
                    f,
                    makeFragmentName(container.getId(), getItemId(position))
            );

            return f;
        }
        return null;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (fragmentTransaction != null) {
            fragmentTransaction.commitNowAllowingStateLoss();
            fragmentTransaction = null;
        }
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @NonNull
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private int getItemId(int pos){
        return pos;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        fragmentTransaction.remove((Fragment) object);
    }

    @Nullable
    protected Fragment getItem(int position){
        if(fragments != null && !fragments.isEmpty() &&
                position > -1 && position < fragments.size())
        {
            return fragments.get(position);
        }
        return null;
    }

    public void updateData(ArrayList<Fragment> fragments){
        this.fragments = fragments;
        notifyDataSetChanged();
    }

}
