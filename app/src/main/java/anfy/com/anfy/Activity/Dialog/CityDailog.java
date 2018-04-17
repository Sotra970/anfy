package anfy.com.anfy.Activity.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.CityAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.CityItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CityDailog extends BaseActivityDialog implements GenericItemClickCallback<CityItem> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.d_title)
    TextView dTitle;

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    private static int countryId;

    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_countries);
        ButterKnife.bind(this);
        dTitle.setText(R.string.select_city);
        init();
        loadCities();
    }

    public static void setCountryId(int countryId) {
        CityDailog.countryId = countryId;
    }

    private void init() {
        cityAdapter = new CityAdapter(null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(cityAdapter);
    }

    public void loadCities(){
        showLoading(true);
        Call<ArrayList<CityItem>> call =
                Injector.Api().cities(countryId);
        call.enqueue(new CallbackWithRetry<ArrayList<CityItem>>(
                call,
                () -> {
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCities();
                    });
                }
        ) {
            @Override
            public void onResponse(Call<ArrayList<CityItem>> call, Response<ArrayList<CityItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<CityItem> countryItems = response.body();
                    cityAdapter.updateData(countryItems);
                }else{
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCities();
                    });
                }
                showLoading(false);
            }
        });
    }


    @Override
    public void onItemClicked(CityItem item) {
        Intent i = new Intent();
        i.putExtra(KEY_CITY_ID, item);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

}
