package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.Adapter.ViewPager.MainViewPagerAdapter;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends TitledFragment {

    private MainViewPagerAdapter pagerAdapter;

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private View mView;

    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, mView);
            init();
        }
        return mView;
    }

    private void init() {
        pagerAdapter = new MainViewPagerAdapter(getFragments(), getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initTabLayout(tabLayout);
    }

    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.getInstance());
        fragments.add(AboutFragment.getInstance());
        fragments.add(ConsultationsFragment.getInstance());
        fragments.add(AlarmsFragment.getInstance());
        return fragments;
    }

    private void initTabLayout(@NonNull final TabLayout tabLayout){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                tab.setCustomView(pagerAdapter.getTabView(i, getContext()));
            }
        }
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(@NonNull TabLayout.Tab tab) {
                        try {
                            ((MainActivity) getActivity()).showSearch(tab.getPosition() == 0);
                        }catch (Exception e){ }
                    }

                    @Override
                    public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
    }

    @Override
    public int getTitleResId() {
        return R.string.home;
    }
}
