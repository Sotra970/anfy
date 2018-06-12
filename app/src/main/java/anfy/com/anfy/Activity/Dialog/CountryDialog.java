package anfy.com.anfy.Activity.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.CountryAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Interface.ResponseListener;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CountryDialog extends BaseActivityDialog implements GenericItemClickCallback<CountryItem> {

    public static final String KEY_COUNTRY_ID = "KEY_COUNTRY_ID";

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.d_title)
    TextView dTitle;

    private CountryAdapter countryAdapter;
    private static ArrayList<CountryItem> countryItems;
    public static boolean FROM_DOCTOR_FRAGMENT = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_countries);
        ButterKnife.bind(this);
        dTitle.setText(R.string.select_country);
        init();
        if(countryItems == null || countryItems.isEmpty()){
            loadCountries();
        }
    }

    private void init() {
        countryAdapter = new CountryAdapter(countryItems, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(countryAdapter);
    }

    public void loadCountries(){
        showLoading(true);
        Call<ArrayList<CountryItem>> call =
                Injector.Api().countries();
        call.enqueue(new CallbackWithRetry<ArrayList<CountryItem>>(
                call,
                () -> {
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCountries();
                    });
                }
        ) {
            @Override
            public void onResponse(Call<ArrayList<CountryItem>> call, Response<ArrayList<CountryItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<CountryItem> countryItems = response.body();
                    if (FROM_DOCTOR_FRAGMENT){
                        CountryItem countryItem = new CountryItem();
                        countryItem.id = -2 ;
                        countryItem.name = "الكل" ;
                        countryItems.add(0,countryItem );
                    }
                    countryAdapter.updateData(countryItems);
                }else{
                    showNoInternet(true, (v)->{
                        showNoInternet(false, null);
                        loadCountries();
                    });
                }
                showLoading(false);
            }
        });
    }


    @Override
    public void onItemClicked(CountryItem item) {
        Intent i = new Intent();
        i.putExtra(KEY_COUNTRY_ID, item);
        setResult(Activity.RESULT_OK, i);
        FROM_DOCTOR_FRAGMENT = false ;
        finish();
    }
}
