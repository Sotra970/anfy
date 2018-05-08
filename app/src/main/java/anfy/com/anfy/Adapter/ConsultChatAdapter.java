package anfy.com.anfy.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import anfy.com.anfy.Activity.ChatActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultChatItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TimeUtils;
import anfy.com.anfy.Util.Utils;
import anfy.com.anfy.ViewHolder.ChatTextVH;
import anfy.com.anfy.ViewHolder.EmptyVH;
import anfy.com.anfy.ViewHolder.ImageVH;

public class ConsultChatAdapter extends GenericAdapter<ConsultChatItem>{

    private final static int VIEW_TYPE_TEXT_ME = 0;
    private final static int VIEW_TYPE_TEXT_OTHER = 1;
    private final static int VIEW_TYPE_IMAGE_ME = 2;
    private final static int VIEW_TYPE_IMAGE_OTHER = 3;
    private final static int VIEW_TYPE_ERROR = 4;
    LinearLayoutManager layoutManager ;
    Activity activity ;

    public ConsultChatAdapter(Activity activity , ArrayList<ConsultChatItem> items , LinearLayoutManager layoutManager , GenericItemClickCallback<ConsultChatItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
        this.layoutManager = layoutManager ;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_IMAGE_ME:
                return new ImageVH(inflate(R.layout.chat_me_image, parent));
            case VIEW_TYPE_IMAGE_OTHER:
                return new ImageVH(inflate(R.layout.chat_other_image, parent));
            case VIEW_TYPE_TEXT_ME:
                return new ChatTextVH(inflate(R.layout.chat_me_text, parent));
            case VIEW_TYPE_TEXT_OTHER:
                return new ChatTextVH(inflate(R.layout.chat_other_text, parent));
        }
        return new EmptyVH(inflate(R.layout.empty_view, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        super.onBindViewHolder(h, position);
        ConsultChatItem item = getItems().get(position) ;
        if (getItemViewType(position) == VIEW_TYPE_TEXT_ME || getItemViewType(position) == VIEW_TYPE_TEXT_OTHER )
        {
            ChatTextVH holder = (ChatTextVH) h;
            if (item!=null){
                holder.msg.setText(item.getText());
                holder.date.setText(TimeUtils.getFromWhen(item.getTime_stamp(),holder.date.getContext()));
            }
        }else if (getItemViewType(position) == VIEW_TYPE_IMAGE_ME || getItemViewType(position) == VIEW_TYPE_IMAGE_OTHER ){
            ImageVH holder = (ImageVH) h;
            holder.date.setText(TimeUtils.getFromWhen(item.getTime_stamp(),holder.date.getContext()));
            Glide.with(holder.imageView.getContext())
                    .load(Utils.getImageUrl(item.getText()))
                    .apply(new RequestOptions().fitCenter())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.imageView);
        }
        h.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(activity)
                    .setMessage(R.string.delete_warning)
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                        ChatActivity.deleteMessage(item.getDbRefrence());
                        getItems().remove(item);
                        notifyItemRemoved(position);
                    }).create().show();
            return false;
        });

    }

    @Override
    public int getItemViewType(int position) {
        ConsultChatItem chatItem = getItem(position);
        if(chatItem != null){
            if (chatItem.isMe())
                return chatItem.getType() == ConsultChatItem.TYPE_TEXT ? VIEW_TYPE_TEXT_ME : VIEW_TYPE_IMAGE_ME;
            else
                return chatItem.getType() == ConsultChatItem.TYPE_TEXT ? VIEW_TYPE_TEXT_OTHER : VIEW_TYPE_IMAGE_OTHER;
        }
        chatItem = null;
        return VIEW_TYPE_ERROR;
    }

    public void  push(ConsultChatItem object){
        if (getItems()==null){
            setItems(new ArrayList<>()) ;
        }
        getItems().add(0,object) ;
        this.notifyItemInserted(0);
        layoutManager.scrollToPosition(0);

    }

}
