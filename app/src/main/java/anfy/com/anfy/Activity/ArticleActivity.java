package anfy.com.anfy.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

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
import anfy.com.anfy.Util.FontSize;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ArticleActivity extends BaseActivity
        implements GenericItemClickCallback<ArticleItem> {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        bindArticle();
        initRecyclers();
        loadArticle();
    }

    private void bindArticle() {
        if(articleItem != null){
            title.setText(articleItem.getTitle());
            title.setText(articleItem.getDepId());
        }
    }

    private void loadArticle() {
        Call<ArticleItem> call = Injector.Api().getArticle(articleItem.getId(), getUserId());
        call.enqueue(new CallbackWithRetry<ArticleItem>(
                call,
                () -> {

                }
        ) {
            @Override
            public void onResponse(@NonNull Call<ArticleItem> call, @NonNull Response<ArticleItem> response) {
                if(response.isSuccessful()){
                    articleItem = response.body();
                    updateContents();
                }
            }
        });
    }

    private void updateContents() {
        if(articleItem != null){
            ArrayList<TopicSegment> segments = new ArrayList<>();
            segments.add(new TopicSegment("dpkjfpdklfl;sfk", getString(R.string.lorem)));
            segments.add(new TopicSegment("dpkjfpdklfl;sfk", getString(R.string.lorem)));
            topicAdapter.updateData(segments);
            //topicAdapter.updateData(articleItem.getContents());
            moreArticlesAdapter.updateData(articleItem.getReleatedArticles());
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
        //ArticleActivity.articleItem = null;
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
}
