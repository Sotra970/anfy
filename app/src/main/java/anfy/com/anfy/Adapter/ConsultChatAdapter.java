package anfy.com.anfy.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Model.ConsultChatItem;
import anfy.com.anfy.R;
import anfy.com.anfy.ViewHolder.EmptyVH;

public class ConsultChatAdapter extends GenericAdapter<ConsultChatItem>{

    private final static int VIEW_TYPE_TEXT_ME = 0;
    private final static int VIEW_TYPE_TEXT_OTHER = 1;
    private final static int VIEW_TYPE_IMAGE_ME = 2;
    private final static int VIEW_TYPE_IMAGE_OTHER = 3;
    private final static int VIEW_TYPE_ERROR = 4;


    public ConsultChatAdapter(ArrayList<ConsultChatItem> items, GenericItemClickCallback<ConsultChatItem> adapterItemClickCallbacks) {
        super(items, adapterItemClickCallbacks);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_IMAGE_ME:
                return new EmptyVH(inflate(R.layout.empty_view, parent));
            case VIEW_TYPE_IMAGE_OTHER:
                return new EmptyVH(inflate(R.layout.empty_view, parent));
            case VIEW_TYPE_TEXT_ME:
                return new EmptyVH(inflate(R.layout.empty_view, parent));
            case VIEW_TYPE_TEXT_OTHER:
                return new EmptyVH(inflate(R.layout.empty_view, parent));
        }
        return new EmptyVH(inflate(R.layout.empty_view, parent));
    }


    @Override
    public int getItemViewType(int position) {
        ConsultChatItem chatItem = getItem(position);
        if(chatItem != null){
            switch (chatItem.getSide()){
                case ConsultChatItem.SIDE_ME:
                    return chatItem.getType() == ConsultChatItem.TYPE_TEXT ? VIEW_TYPE_TEXT_ME : VIEW_TYPE_IMAGE_ME;
                case ConsultChatItem.SIDE_OTHER:
                    return chatItem.getType() == ConsultChatItem.TYPE_TEXT ? VIEW_TYPE_TEXT_OTHER : VIEW_TYPE_IMAGE_OTHER;
            }
        }
        chatItem = null;
        return VIEW_TYPE_ERROR;
    }
}
