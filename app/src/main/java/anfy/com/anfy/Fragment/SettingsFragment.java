package anfy.com.anfy.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.ContactUsActivity;
import anfy.com.anfy.Activity.PrivacyAgreementActivity;
import anfy.com.anfy.Activity.TermsActivity;
import anfy.com.anfy.Adapter.SettingsAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.SettingItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends TitledFragment
        implements GenericItemClickCallback<SettingItem>
{

    private View mView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private SettingsAdapter adapter;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, mView);
            init();
        }
        return mView;
    }

    private void init() {
        adapter = new SettingsAdapter(getSettings(), this, getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public ArrayList<SettingItem> getSettings() {
        ArrayList<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(new SettingItem(R.drawable.notification_menu, R.string.notifications));
        settingItems.add(new SettingItem(R.drawable.contact_terms, R.string.settings_terms));
        settingItems.add(new SettingItem(R.drawable.block_privacy, R.string.settings_privacy_agreement));
        settingItems.add(new SettingItem(R.drawable.telephone_contact, R.string.settings_contact_us));
        return settingItems;
    }

    @Override
    public void onItemClicked(SettingItem item) {
        int title = item.getTitleResId();
        switch (title){
            case R.string.settings_terms:
                openActivity(TermsActivity.class);
                break;
            case R.string.settings_privacy_agreement:
                openActivity(PrivacyAgreementActivity.class);
                break;
            case R.string.settings_contact_us:
                openActivity(ContactUsActivity.class);
                break;
        }
    }

    @Override
    public int getTitleResId() {
        return R.string.nav_settings;
    }
}
