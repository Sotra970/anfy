package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Adapter.DrawerAdapter;
import anfy.com.anfy.Fragment.AboutFragment;
import anfy.com.anfy.Fragment.AlarmsFragment;
import anfy.com.anfy.Fragment.ConsultationsFragment;
import anfy.com.anfy.Fragment.DoctorFragment;
import anfy.com.anfy.Fragment.FavFragment;
import anfy.com.anfy.Fragment.MainFragment;
import anfy.com.anfy.Fragment.NotificationsFragment;
import anfy.com.anfy.Fragment.ProfileFragment;
import anfy.com.anfy.Fragment.SettingsFragment;
import anfy.com.anfy.Fragment.TitledFragment;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.DrawerItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentSwitchActivity
        implements GenericItemClickCallback<DrawerItem>
{

    @BindView(R.id.drawer_recycler)
    RecyclerView drawerRecyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_bg)
    LinearLayout toolbarBg;
    @BindView(R.id.dehaze)
    ImageView navIcon;
    @BindView(R.id.search)
    ImageView searchIcon;
    @BindView(R.id.back)
    ImageView backIcon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.drawer_cover)
    ImageView drawerCover;
    @BindView(R.id.no_data)
    View noData;
    @BindView(R.id.no_internet_layout)
    View noInternet;
    @BindView(R.id.retry)
    View retry;

    
    private DrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initNavDrawer();
        showTitle(R.string.dummy_setting);
        showFragment(MainFragment.getInstance());
    }

    public void initNavDrawer() {
        Glide.with(this).load(R.drawable.splash).into(drawerCover);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(linearLayoutManager);
        if (drawerAdapter == null) {
            ArrayList<DrawerItem> navDrawerItems = getNavDrawerData();
            drawerAdapter =
                    new DrawerAdapter(
                            navDrawerItems,
                            MainActivity.this,
                            MainActivity.this
                    );
        }
        /*DividerItemDecoration horizontalDividerDecoration = new DividerItemDecoration(this);
        drawerRecyclerView.addItemDecoration(horizontalDividerDecoration);*/
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
    }

    private ArrayList<DrawerItem> getNavDrawerData() {
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(R.drawable.house, R.string.nav_home));
        drawerItems.add(new DrawerItem(R.drawable.information_menu, R.string.nav_about));
        drawerItems.add(new DrawerItem(R.drawable.doctor_menu, R.string.nav_doctors));
        drawerItems.add(new DrawerItem(R.drawable.stethoscope_menu, R.string.nav_request_consult));
        drawerItems.add(new DrawerItem(R.drawable.alarm_clock_menu, R.string.nav_alarm));
        drawerItems.add(new DrawerItem(R.drawable.notification_menu, R.string.nav_noti));
        drawerItems.add(new DrawerItem(R.drawable.user_menu, R.string.nav_profile));
        drawerItems.add(new DrawerItem(R.drawable.heart_menu, R.string.nav_fav));
        drawerItems.add(new DrawerItem(R.drawable.settings_menu, R.string.nav_settings));
        return drawerItems;
    }

    @OnClick(R.id.dehaze)
    void openDrawer(){
        drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        drawerAdapter.selectItem(item);
        int title = item.getTextResItem();
        Fragment fragment = null;
        boolean showSearch = false;
        switch (title){
            case R.string.nav_home:
                fragment = MainFragment.getInstance();
                showSearch = true;
                break;
            case R.string.nav_about:
                fragment = AboutFragment.getInstance();
                break;
            case R.string.nav_doctors:
                fragment = DoctorFragment.getInstance();
                break;
            case R.string.nav_request_consult:
                fragment = ConsultationsFragment.getInstance();
                break;
            case R.string.nav_alarm:
                fragment = AlarmsFragment.getInstance();
                break;
            case R.string.nav_noti:
                fragment = NotificationsFragment.getInstance();
                break;
            case R.string.nav_profile:
                fragment = ProfileFragment.getInstance();
                break;
            case R.string.nav_fav:
                fragment = FavFragment.getInstance();
                break;
            case R.string.nav_settings:
                fragment = SettingsFragment.getInstance();
                break;
        }
        if(fragment != null){
            showFragment(fragment);
            showSearch(showSearch);
        }
        drawerLayout.closeDrawers();
    }


    public void showSearch(boolean show){
        title.setTextColor(ResourcesCompat.getColor(getResources(), show ? R.color.def_text_color : R.color.white, null));
        toolbarBg.setBackgroundColor(ResourcesCompat.getColor(getResources(), show ? R.color.white : R.color.iconColor, null));
        navIcon.setColorFilter(ResourcesCompat.getColor(getResources(), show ? R.color.grey_500 : R.color.white, null));
        searchIcon.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showTitle(int resId){
        title.setText(resId);
    }

    public void showNoData(boolean show){
        noData.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showNoInternet(boolean show, View.OnClickListener retryClickListener){
        noInternet.setVisibility(show ? View.VISIBLE : View.GONE);
        retry.setOnClickListener(retryClickListener);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
        setFragmentTitle(fragment);
    }

    @Override
    public void showFragment(Fragment fragment, boolean back, int anim_enter, int exit_anim) {
        super.showFragment(fragment, back, anim_enter, exit_anim);
        setFragmentTitle(fragment);
    }

    @Override
    public void showFragment(Fragment fragment, String tag) {
        super.showFragment(fragment, tag);
        setFragmentTitle(fragment);
    }

    private void setFragmentTitle(Fragment fragment){
        try {
            showTitle(((TitledFragment) fragment).getTitleResId());
        }catch (Exception e){}
    }
}
