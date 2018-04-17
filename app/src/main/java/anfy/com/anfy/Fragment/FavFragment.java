package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import anfy.com.anfy.Activity.ArticleActivity;
import anfy.com.anfy.Adapter.Articles.ArticleAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.ArticleCallbacks;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import anfy.com.anfy.Util.CommonRequests;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class FavFragment extends TitledFragment implements GenericItemClickCallback<ArticleItem>,ArticleCallbacks {

    private final static int REQUEST_FAV_CHANGE = 0;

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
                () -> showNoInternet(true, v -> {
                    showNoInternet(false, null);
                    loadFavs();
                })
        ) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ArticleItem>> call, @NonNull Response<ArrayList<ArticleItem>> response) {
                if(response.isSuccessful()){
                    ArrayList<ArticleItem> articleItems = response.body();
                    showFavs(articleItems);
                    showLoading(false);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        articleAdapter = new ArticleAdapter(null, ArticleAdapter.MODE_LIST,
                false, this, this, getContext());
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    public int getTitleResId() {
        return R.string.nav_fav;
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        openActivityForRes(ArticleActivity.class, REQUEST_FAV_CHANGE);
    }

    @Override
    public void onFavChanged(ArticleItem articleItem) {
        if(articleItem.isFav()){
            articleAdapter.removeItems(Collections.singletonList(articleItem));
            if(articleAdapter.isDataSetEmpty())
                showNoData(true);
            CommonRequests.removeFav(getUserId(), articleItem.getId(), () -> {
                articleItem.setIsFav(false);
            });
        }
    }

    @Override
    public void onShare(ArticleItem articleItem) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FAV_CHANGE && resultCode == Activity.RESULT_OK){
            int id = data.getIntExtra(ArticleActivity.KEY_CHANGED_ARTICLE_ID, -1);
            if(articleAdapter != null && id != -1){
                int b = data.getIntExtra(ArticleActivity.KEY_CHANGED_ARTICLE_STATE, -1);
                if(b == 0){
                    articleAdapter.removeItemWithId(id);
                    showNoData(articleAdapter.isDataSetEmpty());
                }
            }
        }
    }
}
