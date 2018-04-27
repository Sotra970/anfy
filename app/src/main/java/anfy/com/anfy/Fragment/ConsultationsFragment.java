package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.ChatActivity;
import anfy.com.anfy.Activity.Dialog.RequestConsultActivity;
import anfy.com.anfy.Activity.LoginActivity;
import anfy.com.anfy.Adapter.ConsultationsAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultationsFragment extends TitledFragment
        implements GenericItemClickCallback<ConsultationItem> {

    private final static int REQUEST_CONSULT = 60;

    private View mView;

    private ConsultationsAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    public static ConsultationsFragment getInstance() {
        return new ConsultationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_consultations, container, false);
            ButterKnife.bind(this, mView);
            init();
            loadConsults(false);
        }
        return mView;
    }


    private void loadConsults(boolean refresh) {
        if(!refresh) showLoading(true);
        int userID = getUserId();
        if(userID == AppController.NO_USER_ID){
            showNoData(true);
        }else{
            if(!refresh) showLoading(true);
            Call<ArrayList<ConsultationItem>> call =
                    Injector.Api().getConsults(userID);
            call.enqueue(new CallbackWithRetry<ArrayList<ConsultationItem>>(
                    call,
                    () -> {
                        if(refresh) swipeRefreshLayout.setRefreshing(false);
                        else showNoInternet(true, (v)->{
                            showNoInternet(false, null);
                            loadConsults(false);
                        });
                    }
            ) {
                @Override
                public void onResponse(Call<ArrayList<ConsultationItem>> call, Response<ArrayList<ConsultationItem>> response) {
                    if(response.isSuccessful()){
                        ArrayList<ConsultationItem> items = response.body();
                        if(adapter != null){
                            adapter.updateData(items);
                            showNoData(adapter.isDataSetEmpty());
                        }
                    }
                    if(refresh) swipeRefreshLayout.setRefreshing(false);
                    else showLoading(false);
                }
            });
        }


    }

    private void init() {
        adapter = new ConsultationsAdapter(null, this, getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(()->{loadConsults(true);});

    }

    @Override
    public int getTitleResId() {
        return R.string.consultations;
    }

    @Override
    public void onItemClicked(ConsultationItem item) {
        Intent intent = new Intent(getContext() , ChatActivity.class) ;
        intent.putExtra("extra" , item) ;
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void requestConsult(){
        if(getUserId() == AppController.NO_USER_ID){
            openActivity(LoginActivity.class);
        }else{
            openActivityForRes(RequestConsultActivity.class, REQUEST_CONSULT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("consult", "fragment: onActivityResult: request == " + requestCode);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONSULT){
            try {
                ConsultationItem consultationItem =
                        (ConsultationItem) data.getSerializableExtra(RequestConsultActivity.CONSULT_ITEM);
                adapter.addNewItem(consultationItem);
                showNoData(false);
                Log.e("consult", "callback result_ok");
            }catch (Exception e){
                Log.e("consult", e.getMessage() + "");
            }
        }
    }
}
