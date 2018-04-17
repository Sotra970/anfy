package anfy.com.anfy.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Adapter.Articles.ArticleAdapter;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.InfiniteScroll.InfiniteScrollLoaderCallback;
import anfy.com.anfy.InfiniteScroll.InfiniteScrollRecyclerView;
import anfy.com.anfy.Interface.ArticleCallbacks;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.onRequestFailure;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements GenericItemClickCallback<ArticleItem>,ArticleCallbacks, InfiniteScrollLoaderCallback {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler)
    InfiniteScrollRecyclerView recyclerView;

    private ArticleAdapter adapter;
    private String currQuery;

    private boolean dataEnd;

    private int currPage;
    private Call<ArrayList<ArticleItem>> currCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
        initSearch();
    }

    private void init() {
        recyclerView.setScrollLoaderCallback(this);
        adapter = new ArticleAdapter(null, ArticleAdapter.MODE_GRID, false, this, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void initSearch() {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));

        searchView.setBackground(null);

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);

        closeButton.setColorFilter(
                ResourcesCompat.getColor(
                        getResources(),
                        R.color.grey_600,
                        null
                ),
                PorterDuff.Mode.SRC_IN
        );

        //closeButton.setImageResource(R.drawable.ic_clear_white_24px);

        try {
            ImageView searchViewIcon =
                    searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);

            ViewGroup linearLayoutSearchView =(ViewGroup) searchViewIcon.getParent();
            linearLayoutSearchView.removeView(searchViewIcon);

        } catch (Exception e){}

        final EditText et = searchView.findViewById(R.id.search_src_text);

        et.setBackground(null);

        et.setTextColor(
                ResourcesCompat.getColor(
                        getResources(),
                        R.color.grey_600,
                        null
                )
        );

        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        closeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Clear the text from EditText view
                        et.setText("");

                        //Clear query
                        searchView.setQuery("", false);

                        if(adapter != null){
                            adapter.updateData(new ArrayList<>());
                        }
                        if(currCall != null){
                            currCall.cancel();
                            currQuery = null;
                            dataEnd = false;
                        }

                        showLoading(false);
                        showNoData(false);
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                currPage = 0;
                currQuery = query;
                dataEnd = false;
                doSearch();

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void doSearch() {
        if(currQuery != null){
            if(currCall != null) currCall.cancel();
            Integer id = null;
            MyPreferenceManager preferenceManager = new MyPreferenceManager(this);
            if(preferenceManager.getUser() != null){
                id = preferenceManager.getUser().getId();
            }

            currCall = Injector.Api().search(id, currQuery, currPage);

            if(currPage == 0) {
                showLoading(true);
            }else{
                // show small loading
            }

            currCall.enqueue(new CallbackWithRetry<ArrayList<ArticleItem>>(
                    currCall,
                    new onRequestFailure() {
                        @Override
                        public void onFailure() {
                            if(currPage == 0){
                                showNoInternet(true, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showNoInternet(false, null);
                                        doSearch();
                                    }
                                });
                            }
                        }
                    }

            ) {
                @Override
                public void onResponse(Call<ArrayList<ArticleItem>> call, Response<ArrayList<ArticleItem>> response) {
                    if(response.isSuccessful()) {
                        ArrayList<ArticleItem> articleItems = response.body();
                        if (articleItems != null && !articleItems.isEmpty()) {
                            if (adapter != null) {
                                if (currPage == 0) {
                                    adapter.updateData(articleItems);
                                    showLoading(false);
                                } else {
                                    adapter.addItems(articleItems);
                                }
                            }
                            showNoData(false);
                        } else{
                            if(currPage == 0){
                                showNoData(true);
                            }else{
                                dataEnd = true;
                            }
                        }
                    }
                }
            });
        }
    }

    @BindView(R.id.no_data)
    View noData;

    private void showNoData(boolean show){
        noData.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.back)
    void back(){
        finish();
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        openActivity(ArticleActivity.class);
    }

    @Override
    public void onFavChanged(ArticleItem articleItem) {

    }

    @Override
    public void onShare(ArticleItem articleItem) {

    }

    @Override
    public void onLoadMore() {
        if(!dataEnd){
            currPage++;
            doSearch();
        }
    }
}
