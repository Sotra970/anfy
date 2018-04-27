package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.DoctorInfoActivity;
import anfy.com.anfy.Adapter.AboutUsDoctorAdapter;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class AboutFragment extends TitledFragment implements GenericItemClickCallback<DoctorItem> {

    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    private View mView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    private AboutUsDoctorAdapter aboutUsDoctorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_about_us, container, false);
            ButterKnife.bind(this, mView);
            initView();
            loadDoctors(false);
        }
        return mView;
    }

    private void loadDoctors(boolean refresh) {
        if(!refresh){
            showLoading(true);
        }
        Call<ArrayList<DoctorItem>> call = Injector.Api().getAboutUs();
        call.enqueue(new CallbackWithRetry<ArrayList<DoctorItem>>(
                call,
                () -> {
                    if (refresh) swipeRefreshLayout.setRefreshing(false);
                    else showNoInternet(true, v -> {
                        showNoInternet(false, null);
                        loadDoctors(false);
                    });
                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DoctorItem>> call, @NonNull Response<ArrayList<DoctorItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<DoctorItem> doctorItems = response.body();
                    showDoctors(doctorItems);
                }
                if(refresh){
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    showLoading(false);
                }
            }
        });

    }

    private void initView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        aboutUsDoctorAdapter = new AboutUsDoctorAdapter(null, this, getContext());
        recyclerView.setAdapter(aboutUsDoctorAdapter);

        swipeRefreshLayout.setOnRefreshListener(()->{
            loadDoctors(true);
        });
    }

    private void showDoctors(ArrayList<DoctorItem> doctorItems) {
        if(aboutUsDoctorAdapter != null){
            aboutUsDoctorAdapter.updateData(doctorItems);
        }
    }

    @Override
    public int getTitleResId() {
        return R.string.about_us;
    }

    @Override
    public void onItemClicked(DoctorItem item) {
        DoctorInfoActivity.setDoctorItem(item);
        openActivity(DoctorInfoActivity.class);
    }
}
