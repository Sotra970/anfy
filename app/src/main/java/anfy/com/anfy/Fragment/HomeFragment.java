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

import anfy.com.anfy.Adapter.ViewPager.HomeViewPagerAdapter;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private HomeViewPagerAdapter pagerAdapter;


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, mView);
            initDeparts();
        }
        return mView;
    }

    private void init() {
        pagerAdapter = new HomeViewPagerAdapter(null, getFragmentManager(), getContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initDeparts(){
        showLoading(true);
        Call<ArrayList<DepartmentItem>> call = Injector.Api().getDepartments();
        call.enqueue(new CallbackWithRetry<ArrayList<DepartmentItem>>(
                call,
                () -> {
                    showNoInternet(true, v -> {
                        initDeparts();
                    });
                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DepartmentItem>> call,
                                   @NonNull Response<ArrayList<DepartmentItem>> response) {

                if(response.isSuccessful()){
                    ArrayList<DepartmentItem> departmentItems = response.body();
                    initTabs(departmentItems);
                }
                showLoading(false);
            }
        });
    }

    private void initTabs(ArrayList<DepartmentItem> departmentItems) {
        pagerAdapter = new HomeViewPagerAdapter(getFragments(departmentItems), getFragmentManager(), getContext());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initTabLayout(tabLayout, departmentItems);
    }

    private ArrayList<Fragment> getFragments(ArrayList<DepartmentItem> departmentItems){
        ArrayList<Fragment> fragments = new ArrayList<>();
        DepartmentItem departmentItem;
        for(int i = 0; i < departmentItems.size(); i++){
            departmentItem = departmentItems.get(i);
            int deprtId = departmentItem.getId();
            if(deprtId == 0){
                fragments.add(ArticlesHomeFragment.getInstance(departmentItem));
            }else{
                fragments.add(ArticlesFragment.getInstance(departmentItem));
            }
        }
        return fragments;
    }

    private void initTabLayout(@NonNull final TabLayout tabLayout, ArrayList<DepartmentItem> departmentItems){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                tab.setCustomView(pagerAdapter.getTabView(departmentItems.get(i), getContext()));
            }
        }
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(@NonNull TabLayout.Tab tab) {

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
}
