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

import anfy.com.anfy.Adapter.ConsultationsAdapter;
import anfy.com.anfy.Adapter.NotificationAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.NotificationCallbacks;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.Model.NotificationItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends TitledFragment implements NotificationCallbacks {

    private View mView;

    private NotificationAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public static NotificationsFragment getInstance() {
        return new NotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_notifiactions, container, false);
            ButterKnife.bind(this, mView);
            init();
        }
        return mView;
    }

    private void init() {
        ArrayList<NotificationItem> items = new ArrayList<>();
        items.add(new NotificationItem(getString(R.string.lorem), "today", true));
        items.add(new NotificationItem(getString(R.string.lorem), "today", false));
        items.add(new NotificationItem(getString(R.string.lorem), "today", false));
        items.add(new NotificationItem(getString(R.string.lorem), "today", true));

        adapter = new NotificationAdapter(items, this, getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getTitleResId() {
        return R.string.notifications;
    }

    @Override
    public void onNotificationClicked(NotificationItem notificationItem) {

    }

    @Override
    public void onNotificationDeleted(NotificationItem notificationItem) {

    }
}
