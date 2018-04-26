package anfy.com.anfy.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Activity.ArticleActivity;
import anfy.com.anfy.Adapter.Articles.ArticleAdapter;
import anfy.com.anfy.Adapter.Slider.SliderAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.ArticleCallbacks;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.CommonRequests;
import anfy.com.anfy.Util.ShareUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ArticlesHomeFragment extends BaseFragment implements GenericItemClickCallback<ArticleItem>,ArticleCallbacks {

    private final static int REQUEST_FAV_CHANGE = 0;

    private final static int SLIDER_COUNT = 5;

    private View mView;

    private ArticleAdapter articleAdapter;
    private SliderAdapter sliderAdapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.slider)
    ViewPager slider;
    @BindView(R.id.prev)
    View prev;
    @BindView(R.id.next)
    View nxt;

    private DepartmentItem departmentItem;


    public static ArticlesHomeFragment getInstance(DepartmentItem departmentItem) {
        ArticlesHomeFragment fragment = new ArticlesHomeFragment();
        fragment.departmentItem = departmentItem;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_all_articles, container, false);
            ButterKnife.bind(this, mView);
            initSlider();
            initRecycler();
            loadArticles();
        }
        return mView;
    }

    private void initSlider() {
        sliderAdapter = new SliderAdapter(getContext(), null, this);
        slider.setAdapter(sliderAdapter);
    }

    private void initRecycler() {
        articleAdapter = new ArticleAdapter(null, ArticleAdapter.MODE_GRID ,
                true, this, this, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        ArticleActivity.setDepartmentItem(departmentItem);
        openActivityForRes(ArticleActivity.class, REQUEST_FAV_CHANGE);
    }

    @OnClick(R.id.prev)
    void prev(){
        if(slider.getCurrentItem() >= 1){
            slider.setCurrentItem(slider.getCurrentItem() - 1, true);
        }
    }

    @OnClick(R.id.next)
    void next(){
        if(slider.getCurrentItem() + 1 < sliderAdapter.getCount()){
            slider.setCurrentItem(slider.getCurrentItem() + 1, true);
        }
    }

    private void loadArticles() {
        showLoading(true);
        Call<ArrayList<ArticleItem>> call = Injector.Api().getDepartmentArticles(getUserId(), 0);
        call.enqueue(new CallbackWithRetry<ArrayList<ArticleItem>>(
                call,
                () -> {
                    showNoInternet(true, v -> {
                        loadArticles();
                    });
                }
        ) {
            @Override
            public void onResponse(Call<ArrayList<ArticleItem>> call, Response<ArrayList<ArticleItem>> response) {
                if(response.isSuccessful()){
                    AppController.getExecutorService().execute(
                            () -> {
                                ArrayList<ArticleItem> articleItems = response.body();
                                ArrayList<ArticleItem> sliders = new ArrayList<>();
                                for(int i = 0; i < SLIDER_COUNT; i++){
                                    sliders.add(articleItems.get(i));
                                }
                                articleItems.removeAll(sliders);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> {
                                    showSlider(sliders);
                                    showArticles(articleItems);
                                    showLoading(false);
                                });
                            }
                    );
                }
            }
        });
    }

    private void showArticles(ArrayList<ArticleItem> articleItems) {
        if(articleAdapter != null){
            articleAdapter.updateData(articleItems);
        }
    }

    private void showSlider(ArrayList<ArticleItem> sliders){
        if(sliderAdapter != null){
            sliderAdapter.updateData(sliders);
        }
    }

    @Override
    public void onFavChanged(ArticleItem articleItem) {
        if(!articleItem.isFav()){
            CommonRequests.addFav(getUserId(), articleItem.getId(), () -> {
                articleItem.setIsFav(true);
            });
        }else{
            CommonRequests.removeFav(getUserId(), articleItem.getId(), () ->{
                articleItem.setIsFav(false);
            });
        }
    }

    @Override
    public void onShare(ArticleItem articleItem) {
        ShareUtils.shareLink(articleItem.getLink());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FAV_CHANGE && resultCode == Activity.RESULT_OK){
            int id = data.getIntExtra(ArticleActivity.KEY_CHANGED_ARTICLE_ID, -1);
            if(articleAdapter != null && id != -1){
                int b = data.getIntExtra(ArticleActivity.KEY_CHANGED_ARTICLE_STATE, -1);
                if(b == 0 || b == 1){
                    articleAdapter.updateIsFavWithId(id, b == 1);
                }
            }
        }
    }
}
