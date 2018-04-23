package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Fragment.AboutFragment;
import anfy.com.anfy.Fragment.AlarmsFragment;
import anfy.com.anfy.Fragment.ConsultationsFragment;
import anfy.com.anfy.Fragment.DoctorFragment;
import anfy.com.anfy.Fragment.FavFragment;
import anfy.com.anfy.Fragment.HomeFragment;
import anfy.com.anfy.Fragment.MainFragment;
import anfy.com.anfy.Fragment.NotificationsFragment;
import anfy.com.anfy.Fragment.ProfileFragment;
import anfy.com.anfy.Fragment.SettingsFragment;
import anfy.com.anfy.Fragment.TitledFragment;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.DrawerItem;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.sign_out)
    View signOut;

    @BindView(R.id.profile_title)
    TextView profileTitle;

    
    private DrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initNavDrawer();
//        showTitle(R.string.dummy_setting);
        showFragment(HomeFragment.getInstance());
        selectTab(0, true);
    }


    public void refreshImage(){
        runOnUiThread(() -> {
            try{
                Glide.with(getApplicationContext())
                        .load(Utils.getImageUrl(new MyPreferenceManager(getApplicationContext()).getUser().getImage())).into(profileImage);
            }catch (Exception e){}
        });
    }

    public void initNavDrawer() {
        Glide.with(this).load(R.drawable.splash).into(drawerCover);
        MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
        UserModel userModel = preferenceManager.getUser();
        ArrayList<DrawerItem> drawerItems = null;
        if(userModel != null){
            Glide.with(this).load(Utils.getImageUrl(userModel.getImage())).into(profileImage);
            profileTitle.setText(userModel.getName());
            drawerItems = definedUserNavDrawer();
            signOut.setVisibility(View.VISIBLE);
            signOut.setOnClickListener((v)->{
                preferenceManager.clear(true);
            });

        }else{
            Glide.with(this).load(null).into(profileImage);
            profileTitle.setText(R.string.new_user);
            drawerItems = undefinedUserNavDrawer();
            signOut.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(linearLayoutManager);
        if (drawerAdapter == null) {
            drawerAdapter =
                    new DrawerAdapter(
                            drawerItems,
                            MainActivity.this,
                            MainActivity.this
                    );
        }
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerItem toSelect = drawerItems.get(0);
        drawerAdapter.selectItem(toSelect);
    }

    private ArrayList<DrawerItem> undefinedUserNavDrawer(){
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(R.drawable.house, R.string.nav_home));
        drawerItems.add(new DrawerItem(R.drawable.information_menu, R.string.nav_about));
        drawerItems.add(new DrawerItem(R.drawable.doctor_menu, R.string.nav_doctors));
        drawerItems.add(new DrawerItem(R.drawable.alarm_clock_menu, R.string.nav_alarm));
        return drawerItems;
    }

    private ArrayList<DrawerItem> definedUserNavDrawer(){
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
        int id = -1;
        boolean showSearch = false;
        switch (title){
            case R.string.nav_home:
                fragment = HomeFragment.getInstance();
                showSearch = true;
                id = 0;
                break;
            case R.string.nav_about:
                fragment = AboutFragment.getInstance();
                id = 1;
                break;
            case R.string.nav_doctors:
                fragment = DoctorFragment.getInstance();
                break;
            case R.string.nav_request_consult:
                fragment = ConsultationsFragment.getInstance();
                id = 2;
                break;
            case R.string.nav_alarm:
                fragment = AlarmsFragment.getInstance();
                id = 3;
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
            selectTab(selectedPos, false);
            if(id != -1){
                selectTab(id, true);
            }
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

    @OnClick(R.id.search)
    void openSearch(){
        openActivity(SearchActivity.class);
    }

    private int selectedPos = -1;

    public void onTabButtonClicked(View v){
        int id = v.getId();
        Fragment fragment = null;
        boolean showSearch = false;
        int drawerStringId = -1;
        switch (id){
            case R.id.tab1:
                fragment = HomeFragment.getInstance();
                showSearch = true;
                selectTab(selectedPos, false);
                selectTab(0, true);
                drawerStringId = R.string.nav_home;
                break;
            case R.id.tab2:
                fragment = AboutFragment.getInstance();
                selectTab(selectedPos, false);
                selectTab(1, true);
                drawerStringId = R.string.nav_about;
                break;
            case R.id.tab3:
                fragment = ConsultationsFragment.getInstance();
                selectTab(selectedPos, false);
                selectTab(2, true);
                drawerStringId = R.string.nav_request_consult;
                break;
            case R.id.tab4:
                fragment = AlarmsFragment.getInstance();
                selectTab(selectedPos, false);
                selectTab(3, true);
                drawerStringId = R.string.nav_alarm;
                break;
        }
        if(fragment != null){
            showFragment(fragment);
            showSearch(showSearch);
            if(drawerStringId != -1 && drawerAdapter != null){
                drawerAdapter.selectItemByStringId(drawerStringId);
            }
        }
    }

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.image4)
    ImageView image4;
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.title3)
    TextView title3;
    @BindView(R.id.title4)
    TextView title4;

    private void selectTab(int pos, boolean select){
        ImageView imageView = null;
        TextView textView = null;
        switch (pos){
            case 0:
                imageView = image1;
                textView = title1;
                break;
            case 1:
                imageView = image2;
                textView = title2;
                break;
            case 2:
                imageView = image3;
                textView = title3;
                break;
            case 3:
                imageView = image4;
                textView = title4;
                break;
            default:
                return;
        }
        if(imageView != null && textView != null){
            int c = ResourcesCompat.getColor(getResources(), select ? R.color.iconColor : R.color.text_color_3, null);
            imageView.setColorFilter(c);
            textView.setTextColor(c);
        }
        if(select) selectedPos = pos;
    }
}
