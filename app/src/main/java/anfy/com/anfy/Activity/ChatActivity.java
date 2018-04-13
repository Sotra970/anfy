package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Adapter.ConsultChatAdapter;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultChatItem;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements GenericItemClickCallback<ConsultChatItem> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ConsultChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        adapter = new ConsultChatAdapter(null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(ConsultChatItem item) {

    }
}
