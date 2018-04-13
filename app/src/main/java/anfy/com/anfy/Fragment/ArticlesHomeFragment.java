package anfy.com.anfy.Fragment;

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
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ArticlesHomeFragment extends BaseFragment implements GenericItemClickCallback<ArticleItem> {

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
        articleAdapter = new ArticleAdapter(null, ArticleAdapter.MODE_GRID , true, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        ArticleActivity.setDepartmentItem(departmentItem);
        openActivity(ArticleActivity.class);
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
}
