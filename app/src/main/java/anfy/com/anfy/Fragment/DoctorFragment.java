package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Dialog.CityDailog;
import anfy.com.anfy.Activity.Dialog.CountryDialog;
import anfy.com.anfy.Activity.DoctorInfoActivity;
import anfy.com.anfy.Adapter.DoctorAdapter;
import anfy.com.anfy.Adapter.NotificationAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.DoctorCallbacks;
import anfy.com.anfy.Model.CityItem;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.Model.NotificationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class DoctorFragment extends TitledFragment implements DoctorCallbacks {

    private View mView;

    private DoctorAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.country_text)
    TextView country;
    @BindView(R.id.city_text)
    TextView city;

    private int countryId = -1;

    public static DoctorFragment getInstance() {
        return new DoctorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_doctors, container, false);
            ButterKnife.bind(this, mView);
            init();
            loadAllDocs();
        }
        return mView;
    }

    private void loadAllDocs() {
        Call<ArrayList<DoctorItem>> call = Injector.Api().getDoctors();
        call.enqueue(new CallbackWithRetry<ArrayList<DoctorItem>>(
                call, new onRequestFailure() {
            @Override
            public void onFailure() {
                showNoInternet(true, v -> {
                    showNoInternet(false, null);
                    loadAllDocs();
                });
            }
        }
        ) {
            @Override
            public void onResponse(Call<ArrayList<DoctorItem>> call, @NonNull Response<ArrayList<DoctorItem>> response) {
                handleResponse(response);
            }
        });
    }

    private void loadDocs(int country_id) {
        Call<ArrayList<DoctorItem>> call = Injector.Api().getDoctors(country_id);
        call.enqueue(new CallbackWithRetry<ArrayList<DoctorItem>>(
                call, new onRequestFailure() {
            @Override
            public void onFailure() {
                showNoInternet(true, v -> {
                    showNoInternet(false, null);
                    loadDocs(country_id);
                });
            }
        }
        ) {
            @Override
            public void onResponse(Call<ArrayList<DoctorItem>> call, @NonNull Response<ArrayList<DoctorItem>> response) {
                handleResponse(response);
            }
        });
    }

    private void loadDocs(int country_id, int city_id) {
        Call<ArrayList<DoctorItem>> call = Injector.Api().getDoctors(country_id, city_id);
        call.enqueue(new CallbackWithRetry<ArrayList<DoctorItem>>(
                call, () -> showNoInternet(true, v -> {
            showNoInternet(false, null);
            loadDocs(country_id, city_id);
        })
        ) {
            @Override
            public void onResponse(Call<ArrayList<DoctorItem>> call, @NonNull Response<ArrayList<DoctorItem>> response) {
                handleResponse(response);
            }
        });
    }

    private void handleResponse(@NonNull Response<ArrayList<DoctorItem>> response) {
        if(response.isSuccessful()){
            ArrayList<DoctorItem> doctorItems = response.body();
            if(adapter != null){
                adapter.updateData(doctorItems);
                showNoData(adapter.isDataSetEmpty());
            }
        }
        showLoading(false);
    }

    private void init() {
        ArrayList<DoctorItem> items = new ArrayList<>();
        adapter = new DoctorAdapter(null, getContext(),this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDoctorItemClicked(DoctorItem doctorItem) {
        DoctorInfoActivity.setDoctorItem(doctorItem);
        openActivity(DoctorInfoActivity.class);
    }

    @Override
    public void onDoctorCall(DoctorItem doctorItem) {
        String phone = doctorItem.getPhone();
        Utils.callPhone(phone, getContext());
    }

    @Override
    public int getTitleResId() {
        return R.string.nav_doctors;
    }

    @OnClick(R.id.country)
    void selectCountry(){
        openActivityForRes(CountryDialog.class, AppController.REQUEST_COUNTRY);
    }

    @OnClick(R.id.city)
    void selectCity(){
        if(countryId != -1) {
            CityDailog.setCountryId(countryId);
            openActivityForRes(CityDailog.class, AppController.REQUEST_CITY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == AppController.REQUEST_COUNTRY){
                try {
                    CountryItem countryItem = (CountryItem)
                            data.getSerializableExtra(CountryDialog.KEY_COUNTRY_ID);
                    country.setText(countryItem.getName());
                    countryId = countryItem.getId();
                    loadDocs(countryId);
                }catch (Exception e){

                }
            }else if(requestCode == AppController.REQUEST_CITY){
                try {
                    CityItem cityItem = (CityItem)
                            data.getSerializableExtra(CityDailog.KEY_CITY_ID);
                    city.setText(cityItem.getName());
                    int cityId = cityItem.getId();
                    loadDocs(countryId, cityId);
                }catch (Exception e){

                }
            }
        }
    }
}
