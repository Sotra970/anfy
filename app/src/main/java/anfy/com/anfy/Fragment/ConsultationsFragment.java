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

import anfy.com.anfy.Activity.Dialog.RequestConsultActivity;
import anfy.com.anfy.Adapter.ConsultationsAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConsultationsFragment extends TitledFragment implements GenericItemClickCallback<ConsultationItem> {

    private View mView;

    private ConsultationsAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

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
        }
        return mView;
    }

    private void init() {
        ArrayList<ConsultationItem> items = new ArrayList<>();
        items.add(new ConsultationItem("dfdsfsfffd", "today", "8888"));
        items.add(new ConsultationItem("dfdsfsfffd", "today", "8888"));
        items.add(new ConsultationItem("dfdsfsfffd", "today", "8888"));
        adapter = new ConsultationsAdapter(items, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getTitleResId() {
        return R.string.consultations;
    }

    @Override
    public void onItemClicked(ConsultationItem item) {

    }

    @OnClick(R.id.fab)
    void requestConsult(){
        openActivity(RequestConsultActivity.class);
    }
}
