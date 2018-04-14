package anfy.com.anfy.Adapter.Articles;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.GenericAdapter;
import anfy.com.anfy.App.MyPreferenceManager;
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

    public ArticleAdapter(ArrayList<ArticleItem> items, int mode, boolean home, GenericItemClickCallback<ArticleItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
        this.mode = mode;
        this.home = home;
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
            Glide.with(vh.image.getContext())
                    .load(Utils.getImageUrl(articleItem.getCover()))
                    .into(vh.image);
            if(vh.iconContainer != null){
                vh.iconContainer.setVisibility(home ? View.VISIBLE : View.GONE);
            }
            if(vh.fav != null && vh.share != null){
                vh.fav.setOnClickListener(v -> {

                });
            }
        }
    }
}
