package anfy.com.anfy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.TopicSegment;
import anfy.com.anfy.R;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Util.FontSize;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.TopicSegmentVH;

public class TopicAdapter extends GenericAdapter<TopicSegment> {

    private Context context;

    public TopicAdapter(ArrayList<TopicSegment> items, Context context) {
        super(items, null);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(R.layout.topic_segment, parent);
        return new TopicSegmentVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TopicSegment segment = getItem(position);
        if (segment != null) {
            TopicSegmentVH vh = (TopicSegmentVH) holder;
            vh.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize.getHeaderSize(context));
            vh.title.setText(segment.getTitle());
            vh.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize.getBodySize(context));
            vh.content.setHtml(segment.getContent()+"" , new HtmlHttpImageGetter(vh.content , null , true));
        }
    }
}