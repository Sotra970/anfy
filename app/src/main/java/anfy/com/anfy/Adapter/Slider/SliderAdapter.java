package anfy.com.anfy.Adapter.Slider;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.R;

/**
 * Created by Ahmed on 9/15/2017.
 */

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ArticleItem> articleItems;
    private GenericItemClickCallback<ArticleItem> clickCallback;

    public SliderAdapter(Context context, ArrayList<ArticleItem> articleItems, GenericItemClickCallback<ArticleItem> clickCallback) {
        this.context = context;
        this.articleItems = articleItems;
        this.clickCallback = clickCallback;
    }

    @Override
    public int getCount() {
        return articleItems == null ? 0 : articleItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ArticleItem articleItem = articleItems.get(position);
        View v = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.slider_item, container, false);
        v.setOnClickListener(v1 -> {
            if(clickCallback != null){
                clickCallback.onItemClicked(articleItem);
            }
        });
        TextView title = v.findViewById(R.id.title);
        title.setText(articleItem.getTitle());
        ImageView imageView = v.findViewById(R.id.image);
        Glide.with(context)
                .load(articleItem.getCover())
                .into(imageView);
        container.addView(v);
        return v;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateData(ArrayList<ArticleItem> articleItems){
        this.articleItems = articleItems;
        notifyDataSetChanged();
    }
}
