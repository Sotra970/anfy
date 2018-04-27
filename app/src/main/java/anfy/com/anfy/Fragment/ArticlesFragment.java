package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.ArticleActivity;
import anfy.com.anfy.Adapter.Articles.ArticleAdapter;
import anfy.com.anfy.Decorator.DividerItemDecoration;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class ArticlesFragment extends BaseFragment implements GenericItemClickCallback<ArticleItem> {

    private final static int REQUEST_FAV_CHANGE = 0;

    private View mView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;


    private DepartmentItem departmentItem;

    private ArticleAdapter articleAdapter;

    public static ArticlesFragment getInstance(DepartmentItem departmentItem) {
        ArticlesFragment fragment = new ArticlesFragment();
        fragment.departmentItem = departmentItem;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_articles, container, false);
            ButterKnife.bind(this, mView);
            init();
            loadArticles(false);
        }
        return mView;
    }


    private void loadArticles(boolean refresh) {
        if(!refresh) showLoading(true);
        if(departmentItem != null){
            if(!refresh) showLoading(true);
            Call<ArrayList<ArticleItem>> call = Injector.Api().getDepartmentArticles(getUserId(), departmentItem.getId());
            call.enqueue(new CallbackWithRetry<ArrayList<ArticleItem>>(
                    call,
                    () -> {
                        if(refresh) swipeRefreshLayout.setRefreshing(false);
                        else{
                            showNoInternet(true, v -> {
                                showNoInternet(false, null);
                                loadArticles(false);
                            });
                        }
                    }
            ) {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ArticleItem>> call, @NonNull Response<ArrayList<ArticleItem>> response) {
                    if(response.isSuccessful()){
                        ArrayList<ArticleItem> articleItems = response.body();
                        showArticles(articleItems);
                    }
                    if(refresh) swipeRefreshLayout.setRefreshing(false);
                    else showLoading(false);
                }
            });
        }
    }

    private void showArticles(ArrayList<ArticleItem> articleItems) {
        if(articleAdapter != null){
            articleAdapter.updateData(articleItems);
        }
    }

    private void init() {
        articleAdapter = new ArticleAdapter(null, ArticleAdapter.MODE_GRID,false, this,null, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(articleAdapter);
        swipeRefreshLayout.setOnRefreshListener(()->{loadArticles(true);});
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.openArticle(item, getContext());
    }
}
