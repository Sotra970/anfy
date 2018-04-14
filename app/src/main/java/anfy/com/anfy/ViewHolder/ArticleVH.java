package anfy.com.anfy.ViewHolder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleVH extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.title)
    public TextView title;
    @Nullable
    @BindView(R.id.share)
    public ImageView share;
    @Nullable
    @BindView(R.id.fav)
    public ImageView fav;
    @Nullable
    @BindView(R.id.icon_container)
    public View iconContainer;

    public ArticleVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
