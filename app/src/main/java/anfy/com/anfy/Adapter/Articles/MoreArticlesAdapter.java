package anfy.com.anfy.Adapter.Articles;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.GenericAdapter;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.ArticleVH;

public class MoreArticlesAdapter extends GenericAdapter<ArticleItem> {

    public MoreArticlesAdapter(ArrayList<ArticleItem> items, GenericItemClickCallback<ArticleItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflate(R.layout.more_articles_item, parent);
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
        }
    }
}
