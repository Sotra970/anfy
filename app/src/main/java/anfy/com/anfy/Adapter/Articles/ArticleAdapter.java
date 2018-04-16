package anfy.com.anfy.Adapter.Articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.GenericAdapter;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Interface.ArticleCallbacks;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.CommonRequests;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.ArticleVH;

public class ArticleAdapter extends GenericAdapter<ArticleItem> {

    public final static int MODE_GRID = 0;
    public final static int MODE_LIST = 1;

    private int mode;
    private boolean home;
    private ArticleCallbacks articleCallbacks;
    private Context context;

    public ArticleAdapter(ArrayList<ArticleItem> items,
                          int mode,
                          boolean home,
                          GenericItemClickCallback<ArticleItem> adapterItemClickCallbacks,
                          ArticleCallbacks articleCallbacks,
                          Context context)
    {
        super(items, adapterItemClickCallbacks);
        this.mode = mode;
        this.home = home;
        this.articleCallbacks = articleCallbacks;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflate(mode == MODE_LIST ? R.layout.article_item : R.layout.article_grid_item, parent);
        return new ArticleVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ArticleItem articleItem = getItem(position);
        if(articleItem != null){
            ArticleVH vh = (ArticleVH) holder;
            vh.title.setText(articleItem.getTitle());
            Glide.with(context)
                    .load(Utils.getImageUrl(articleItem.getCover()))
                    .into(vh.image);

            if(vh.iconContainer != null){
                vh.iconContainer.setVisibility(home ? View.VISIBLE : View.GONE);
            }
            if(vh.fav != null && vh.share != null){
                vh.fav.setColorFilter(ResourcesCompat.getColor(context.getResources(), articleItem.isFav() ? R.color.icon_active : R.color.icon_idle, null));
                vh.fav.setOnClickListener(v -> {
                    if(articleCallbacks != null){
                        vh.fav.setColorFilter(ResourcesCompat.getColor(context.getResources(), articleItem.isFav() ? R.color.icon_idle : R.color.icon_active, null));
                        articleCallbacks.onFavChanged(articleItem);
                    }
                });
                vh.share.setOnClickListener(v -> {
                    if(articleCallbacks != null){
                        articleCallbacks.onShare(articleItem);
                    }
                });
            }
        }
    }
}
