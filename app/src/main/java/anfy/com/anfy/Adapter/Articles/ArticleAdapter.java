package anfy.com.anfy.Adapter.Articles;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import anfy.com.anfy.Activity.LoginActivity;
import anfy.com.anfy.Activity.PhoneLoginActivity;
import anfy.com.anfy.Adapter.GenericAdapter;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Interface.ArticleCallbacks;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;
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
        View view = null;
        if(mode == MODE_LIST){
            view = inflate(R.layout.article_item , parent);
        }else{
            view = inflate(home ? R.layout.article_grid_item_homr : R.layout.article_grid_item, parent);
        }
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
            if(vh.fav != null){
                vh.fav.setColorFilter(ResourcesCompat.getColor(context.getResources(), articleItem.isFav() ? R.color.icon_active : R.color.icon_idle, null));
                vh.fav.setOnClickListener(v -> {
                    if(articleCallbacks != null){
                        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
                        if(myPreferenceManager.getUser() == null){
                            Intent i = new Intent(context, LoginActivity.class);
                            context.startActivity(i);
                        }else{
                            vh.fav.setColorFilter(ResourcesCompat.getColor(context.getResources(), articleItem.isFav() ? R.color.icon_idle : R.color.icon_active, null));
                            articleCallbacks.onFavChanged(articleItem);
                        }
                    }
                });
            }
            if(vh.share != null){
                vh.share.setOnClickListener(v -> {
                    if(articleCallbacks != null){
                        articleCallbacks.onShare(articleItem);
                    }
                });
            }
        }
    }

    public void updateIsFavWithId(int id, boolean isFav) {
        ArrayList<ArticleItem> articleItems  = getItems();
        if(articleItems != null && !articleItems.isEmpty()){
            for(int i = 0; i < articleItems.size(); i++){
                if(articleItems.get(i).getId() == id){
                    articleItems.get(i).setIsFav(isFav);
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public void removeItemWithId(int id){
        ArrayList<ArticleItem> articleItems  = getItems();
        if(articleItems != null && !articleItems.isEmpty()){
            int pos = -1;
            for(int i = 0; i < articleItems.size(); i++){
                if(articleItems.get(i).getId() == id){
                    pos = i;
                    break;
                }
            }
            if(pos != -1){
                articleItems.remove(pos);
                notifyItemRemoved(pos);

            }
        }
    }

    public void addItems(ArrayList<ArticleItem> articleItems){
        if(getItems() == null){
            setItems(new ArrayList<>());
        }
        int startPos = getItems().size() - 1;
        getItems().addAll(articleItems);
        int endPos = getItems().size() - 1;
        notifyItemRangeChanged(startPos, endPos);
    }

    public void changeIsFavWithIds(HashMap<Integer, Boolean> changed) {
        if(getItems() != null){
            int i = 0;
            for(ArticleItem item : getItems()){
                int id = item.getId();
                Boolean b = changed.get(id);
                if(b != null){
                    item.setIsFav(b);
                    notifyItemChanged(i);
                }
                i++;
            }
        }
    }
}
