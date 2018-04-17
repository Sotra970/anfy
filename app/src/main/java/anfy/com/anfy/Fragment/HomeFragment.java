package anfy.com.anfy.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.Adapter.ViewPager.HomeViewPagerAdapter;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends TitledFragment {

    private View mView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tab_container)
    View tabContainer;

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
            tabContainer.setVisibility(View.GONE);
            init();
            initDeparts();
        }
        return mView;
    }

    private void init() {
        if(getFragmentManager() != null){
            pagerAdapter = new HomeViewPagerAdapter(null, getFragmentManager(), getContext());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void initDeparts(){
        showLoading(true);
        Call<ArrayList<DepartmentItem>> call = Injector.Api().getDepartments();
        call.enqueue(new CallbackWithRetry<ArrayList<DepartmentItem>>(
                call,
                () -> {
                    showNoInternet(true, v -> {
                        showNoInternet(false, null);
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
                    tabContainer.setVisibility(View.VISIBLE);
                }
                showLoading(false);
            }
        });
    }

    private void initTabs(ArrayList<DepartmentItem> departmentItems) {
        if(pagerAdapter != null){
            pagerAdapter.updateData(getFragments(departmentItems));
        }
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
            if(tab != null && getContext() != null){
                tab.setCustomView(pagerAdapter.getTabView(departmentItems.get(i), getContext()));
                if(i == 0){
                    selectTab(tab, true);
                }
            }
        }
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(@NonNull TabLayout.Tab tab) {
                        selectTab(tab, true);
                    }

                    @Override
                    public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                        selectTab(tab, false);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                }
        );
    }

    private void selectTab(TabLayout.Tab tab, boolean select){
        /*try {
            View v = tab.getCustomView();
            ImageView imageView = v.findViewById(R.id.image);
            TextView textView = v.findViewById(R.id.title);
            int c = ResourcesCompat.getColor(getResources(), select ? R.color.iconColor : R.color.text_color_3, null);
            imageView.setColorFilter(c);
            textView.setTextColor(c);
        }catch(Exception e){ }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getTitleResId() {
        return R.string.home;
    }
}
