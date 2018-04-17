package anfy.com.anfy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Adapter.Articles.MoreArticlesAdapter;
import anfy.com.anfy.Adapter.TopicAdapter;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.Model.TopicSegment;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.CallbackWithRetry;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.CommonRequests;
import anfy.com.anfy.Util.FontSize;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ArticleActivity extends BaseActivity
        implements GenericItemClickCallback<ArticleItem>
{

    private static ArticleItem articleItem;
    private static DepartmentItem departmentItem;

    private MoreArticlesAdapter moreArticlesAdapter;
    private TopicAdapter topicAdapter;

    @BindView(R.id.recycler_topic)
    RecyclerView topicRecycler;
    @BindView(R.id.recycler_more)
    RecyclerView moreRecycler;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.cover)
    ImageView cover;
    @BindView(R.id.fav)
    ImageView fav;
    @BindView(R.id.topic_container)
    View topicContainer;
    @BindView(R.id.more_container)
    View moreContainer;

    public final static String KEY_CHANGED_ARTICLE_ID = "ID";
    public final static String KEY_CHANGED_ARTICLE_STATE = "STATE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        moreContainer.setVisibility(View.GONE);
        topicContainer.setVisibility(View.GONE);
        bindArticle();
        initRecyclers();
        loadArticle();
    }

    private void bindArticle() {
        if(articleItem != null){
            Glide.with(this).load(Utils.getImageUrl(articleItem.getCover())).into(cover);
            title.setText(articleItem.getTitle());
            fav.setColorFilter(ResourcesCompat.getColor(getResources(), articleItem.isFav() ? R.color.icon_active : R.color.icon_idle, null));
        }
    }

    private void loadArticle() {
        showLoading(true);
        Call<ArticleItem> call = Injector.Api().getArticle(articleItem.getId(), getUserId());
        call.enqueue(new CallbackWithRetry<ArticleItem>(
                call,
                () -> {
                    showNoInternet(true, (v -> {
                        showNoInternet(false, null);
                        loadArticle();
                    }));
                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ArticleItem> call, @NonNull Response<ArticleItem> response) {
                if(response.isSuccessful()){
                    articleItem = response.body();
                    updateContents();
                }
                showLoading(false);
            }
        });
    }

    private void updateContents() {
        if(articleItem != null){
            if(articleItem.getDepartmentItem() != null) {
                category.setText(articleItem.getDepartmentItem().getName());
            }
            topicAdapter.updateData(articleItem.getContents());
            topicContainer.setVisibility(topicAdapter.isDataSetEmpty() ? View.GONE : View.VISIBLE);
            moreArticlesAdapter.updateData(articleItem.getReleatedArticles());
            moreContainer.setVisibility(moreArticlesAdapter.isDataSetEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private void initRecyclers() {
        // content recycler
        topicAdapter = new TopicAdapter(null, this);
        topicRecycler.setLayoutManager(new LinearLayoutManager(this));
        topicRecycler.setAdapter(topicAdapter);

        // related recycler
        moreArticlesAdapter = new MoreArticlesAdapter(null, this);
        moreRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        moreRecycler.setAdapter(moreArticlesAdapter);
    }

    public static void setArticleItem(ArticleItem articleItem) {
        ArticleActivity.articleItem = articleItem;
    }

    public static void setDepartmentItem(DepartmentItem departmentItem) {
        ArticleActivity.departmentItem = departmentItem;
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        ArticleActivity.setArticleItem(item);
        ArticleActivity.setDepartmentItem(departmentItem);
        openActivity(ArticleActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.exit)
    void exit(){
        finish();
    }

    @OnClick(R.id.add)
    void increaseFontSize(){
        FontSize.increaseFontSize(this);
        if(topicAdapter != null){
            topicAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.subtract)
    void dencreaseFontSize(){
        FontSize.decreaseFontSize(this);
        if(topicAdapter != null){
            topicAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.fav)
    void switchFav(){
        if(!articleItem.isFav()){
            fav.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.icon_active, null));
            CommonRequests.addFav(getUserId(), articleItem.getId(), () -> {
                articleItem.setIsFav(true);
                Intent i = new Intent();
                i.putExtra(KEY_CHANGED_ARTICLE_ID, articleItem.getId());
                i.putExtra(KEY_CHANGED_ARTICLE_STATE, 1);
                setResult(Activity.RESULT_OK, i);
            });
        }else{
            fav.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.icon_idle, null));
            CommonRequests.removeFav(getUserId(), articleItem.getId(), () -> {
                articleItem.setIsFav(false);
            });
            Intent i = new Intent();
            i.putExtra(KEY_CHANGED_ARTICLE_ID, articleItem.getId());
            i.putExtra(KEY_CHANGED_ARTICLE_STATE, 0);
            setResult(Activity.RESULT_OK, i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
