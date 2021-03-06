package anfy.com.anfy.Fragment;

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

import java.util.ArrayList;

import anfy.com.anfy.Activity.DoctorInfoActivity;
import anfy.com.anfy.Adapter.DoctorAdapter;
import anfy.com.anfy.Adapter.NotificationAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.DoctorCallbacks;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.Model.NotificationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorFragment extends TitledFragment implements DoctorCallbacks {

    private View mView;

    private DoctorAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public static DoctorFragment getInstance() {
        return new DoctorFragment();
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
        ArrayList<DoctorItem> items = new ArrayList<>();
        /*items.add(new DoctorItem("dssddsfdsfs", "0000"));
        items.add(new DoctorItem("dssddsfdsfs", "0000"));
        items.add(new DoctorItem("dssddsfdsfs", "0000"));*/
        adapter = new DoctorAdapter(items, getContext(),this);
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
}
