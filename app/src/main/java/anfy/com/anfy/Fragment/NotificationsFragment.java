package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.NotificationAdapter;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.NotificationCallbacks;
import anfy.com.anfy.Model.NotificationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class NotificationsFragment extends TitledFragment implements NotificationCallbacks {

    private View mView;

    private NotificationAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

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
        adapter = new NotificationAdapter(null, this, getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(()->{getData(true);});
        getData(false);
    }

    @Override
    public int getTitleResId() {
        return R.string.notifications;
    }

    @Override
    public void onNotificationClicked(int pos  , NotificationItem notificationItem) {
        read_notification(pos , notificationItem);
    }

    private void read_notification(final int pos ,final NotificationItem notificationItem) {
        showLoading(true);
        Call<ResponseBody> call = Injector.Api().readNotification(notificationItem.getId()) ;
        call.enqueue(new CallbackWithRetry<ResponseBody>(call, new onRequestFailure() {
            @Override
            public void onFailure() {
                showNoInternet(true, (v)->{
                    showNoInternet(false, null);
                    read_notification(pos , notificationItem);
                });
            }
        }) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    notificationItem.setRead(1);
                    adapter.updateItem(pos , notificationItem);
                }
                showLoading(false);
            }
        });
    }

    private void delete_notification(final int pos ,final NotificationItem notificationItem) {
        showLoading(true);
        Call<ResponseBody> call = Injector.Api().deletedNotification(notificationItem.getId()) ;
        call.enqueue(new CallbackWithRetry<ResponseBody>(call, new onRequestFailure() {
            @Override
            public void onFailure() {
                showNoInternet(true, (v)->{
                    showNoInternet(false, null);
                    delete_notification(pos , notificationItem);
                });
            }
        }) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    adapter.removeItem(pos);
                    showNoData(adapter.isDataSetEmpty());
                }
                showLoading(false);
            }
        });
    }

    @Override
    public void onNotificationDeleted(int pos , NotificationItem notificationItem) {
        delete_notification(pos , notificationItem) ;
    }


    void getData(boolean refresh){
        if(!refresh) showLoading(true);
        Call<ArrayList<NotificationItem>> call =
                Injector.Api().getNotifications(new MyPreferenceManager(getContext()).getUser().getId()+"");
        call.enqueue(new CallbackWithRetry<ArrayList<NotificationItem>>(
                call,
                () -> {
                    if(refresh)  swipeRefreshLayout.setRefreshing(false);
                    else{
                        showNoInternet(true, (v)->{
                            showNoInternet(false, null);
                            getData(false);
                        });
                    }
                }
        ) {
            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call, Response<ArrayList<NotificationItem>> response) {
                if(response.isSuccessful()&& response.body() !=null){
                    ArrayList<NotificationItem> items = response.body();
                    if(adapter != null){
                        adapter.updateData(items);
                        showNoData(adapter.isDataSetEmpty());
                    }
                }else {
                    showNoData(true);
                }
                showLoading(false);
            }
        });
    }
}
