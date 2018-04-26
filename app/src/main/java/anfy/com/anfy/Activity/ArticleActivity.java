package anfy.com.anfy.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
import anfy.com.anfy.Util.ShareUtils;
import anfy.com.anfy.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ArticleActivity extends BaseActivity
        implements GenericItemClickCallback<ArticleItem>
{

    private  ArticleItem articleItem;

    public static String KEY_ARTICLE = "KEY_ARTICLE";

    private static HashMap<Integer, Boolean> changedArticles;

    private TopicAdapter topicAdapter;

    @BindView(R.id.recycler_topic)
    RecyclerView topicRecycler;

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

    @BindView(R.id.related_article_1_image)
    ImageView relatedImage1;
    @BindView(R.id.related_article_2_image)
    ImageView relatedImage2;
    @BindView(R.id.related_article_3_image)
    ImageView relatedImage3;
    @BindView(R.id.related_article_1_title)
    TextView relatedTitle1;
    @BindView(R.id.related_article_2_title)
    TextView relatedTitle2;
    @BindView(R.id.related_article_3_title)
    TextView relatedTitle3;
    @BindView(R.id.related_1)
    View related1;
    @BindView(R.id.related_2)
    View related2;
    @BindView(R.id.related_3)
    View related3;

    public static HashMap<Integer, Boolean> getChangedArticles() {
        return changedArticles;
    }

    private View.OnClickListener relatedClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    int article = -1;
                    switch (id){
                        case R.id.related_1:
                            article = 0;
                            break;
                        case R.id.related_2:
                            article = 1;
                            break;
                        case R.id.related_3:
                            article = 2;
                            break;
                    }
                    if(article != -1){
                        try {
                            ArticleItem r = articleItem.getReleatedArticles().get(article);
                            openArticle(r, ArticleActivity.this);
                        }catch (Exception e){

                        }
                    }
                }
            };

    public final static String KEY_CHANGED_ARTICLE_ID = "ID";
    public final static String KEY_CHANGED_ARTICLE_STATE = "STATE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        moreContainer.setVisibility(View.GONE);
        topicContainer.setVisibility(View.GONE);
        getArticle();
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

            ArrayList<ArticleItem> moreArticles = articleItem.getReleatedArticles();
            if(moreArticles != null && !moreArticles.isEmpty()){
                related1.setOnClickListener(relatedClickListener);
                related2.setOnClickListener(relatedClickListener);
                related3.setOnClickListener(relatedClickListener);
                int count = moreArticles.size();
                Log.e("articleActivity", "relatedArticles count == "+ count);
                switch (count){
                    case 1:
                        related1.setVisibility(View.VISIBLE);
                        related2.setVisibility(View.INVISIBLE);
                        related3.setVisibility(View.INVISIBLE);
                        bindArticle(moreArticles.get(0), relatedTitle1, relatedImage1);
                        break;
                    case 2:
                        related1.setVisibility(View.VISIBLE);
                        related2.setVisibility(View.VISIBLE);
                        related3.setVisibility(View.INVISIBLE);
                        bindArticle(moreArticles.get(0), relatedTitle1, relatedImage1);
                        bindArticle(moreArticles.get(1), relatedTitle2, relatedImage2);
                        break;
                    default:
                        related1.setVisibility(View.VISIBLE);
                        related2.setVisibility(View.VISIBLE);
                        related3.setVisibility(View.VISIBLE);
                        bindArticle(moreArticles.get(0), relatedTitle1, relatedImage1);
                        bindArticle(moreArticles.get(1), relatedTitle2, relatedImage2);
                        bindArticle(moreArticles.get(2), relatedTitle3, relatedImage3);
                        break;
                }
                moreContainer.setVisibility(View.VISIBLE);
            }else{
                moreContainer.setVisibility(View.GONE);
            }

        }
    }

    private void bindArticle(ArticleItem articleItem, TextView textView, ImageView imageView){
        textView.setText(articleItem.getTitle());
        Glide.with(this).load(Utils.getImageUrl(articleItem.getCover())).into(imageView);
    }

    private void initRecyclers() {
        // content recycler
        topicAdapter = new TopicAdapter(null, this);
        topicRecycler.setLayoutManager(new LinearLayoutManager(this));
        topicRecycler.setAdapter(topicAdapter);
    }

    @Override
    public void onItemClicked(ArticleItem item) {
        openArticle(articleItem, this);
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
        if(getUser() == null){
            openActivity(LoginActivity.class);
        }else{
            if(!articleItem.isFav()){
                fav.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.icon_active, null));
                CommonRequests.addFav(getUserId(), articleItem.getId(), () -> {
                    articleItem.setIsFav(true);
                    addChangedArticle(articleItem.getId(), true);
                });
            }else{
                fav.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.icon_idle, null));
                CommonRequests.removeFav(getUserId(), articleItem.getId(), () -> {
                    articleItem.setIsFav(false);
                });
                addChangedArticle(articleItem.getId(), false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @OnClick(R.id.share)
    void share(){
        ShareUtils.shareLink(articleItem.getLink());
    }


    public void getArticle() {
        boolean finish = true;
        Intent i = getIntent();
        if(i != null){
            Object a = i.getSerializableExtra(KEY_ARTICLE);
            if(a != null){
                articleItem = (ArticleItem) a;
                finish = false;
            }
        }
        if(finish) finish();
    }

    public static void openArticle(ArticleItem articleItem, Context context){
        Intent in = new Intent(context, ArticleActivity.class);
        in.putExtra(KEY_ARTICLE, articleItem);
        context.startActivity(in);
    }

    public interface ActivityFavChangeListener extends Serializable{
        void onFavChanged(int articleId, boolean isFav);
    }

    private static void addChangedArticle(int id, boolean isFav){
        if(changedArticles == null){
            resetArticles();
        }
        changedArticles.put(id, isFav);
    }

    public static void resetArticles(){
        changedArticles = new HashMap<>();
    }
}
