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

import anfy.com.anfy.Activity.ArticleActivity;
import anfy.com.anfy.Adapter.Articles.ArticleAdapter;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class FavFragment extends TitledFragment implements GenericItemClickCallback<ArticleItem> {

    private View mView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ArticleAdapter articleAdapter;

    public static FavFragment getInstance() {
        return new FavFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_fav, container, false);
            ButterKnife.bind(this, mView);
            init();
            loadFavs();
        }
        return mView;
    }

    private void loadFavs() {
        showLoading(true);
        Call<ArrayList<ArticleItem>> call = Injector.Api().getUserFavourites(getUserId());
        call.enqueue(new CallbackWithRetry<ArrayList<ArticleItem>>(
                call,
                () -> showNoInternet(true, v -> loadFavs())
        ) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ArticleItem>> call, @NonNull Response<ArrayList<ArticleItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<ArticleItem> articleItems = response.body();
                    showFavs(articleItems);
                }
            }
        });
    }

    private void showFavs(ArrayList<ArticleItem> articleItems) {
        if(articleAdapter != null){
            articleAdapter.updateData(articleItems);
            showNoData(articleAdapter.isDataSetEmpty());
        }
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        articleAdapter = new ArticleAdapter(null, ArticleAdapter.MODE_LIST, this);
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    public int getTitleResId() {
        return R.string.nav_fav;
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        openActivity(ArticleActivity.class);
    }
}
