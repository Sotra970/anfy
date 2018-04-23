package anfy.com.anfy.Adapter;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anfy.com.anfy.Interface.GenericItemClickCallback;
import anfy.com.anfy.Interface.NotificationCallbacks;
import anfy.com.anfy.Model.NotificationItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.TimeUtils;
import anfy.com.anfy.ViewHolder.NotificationVH;

/**
 * Created by Ahmed on 8/18/2017.
 */

public class NotificationAdapter extends GenericAdapter<NotificationItem>
{

    private NotificationCallbacks notificationCallbacks;
    private Context context;

    public NotificationAdapter(ArrayList<NotificationItem> items,
                               NotificationCallbacks callbacks,
                               Context context)
    {
        super(items, null);
        this.notificationCallbacks = callbacks;
        this.context = context;
    }

    @Override
    public NotificationVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflate(R.layout.notification_item, parent);
        return new NotificationVH(v);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
        final NotificationItem notificationItem =  getItem(position);
        if(notificationItem != null){
            NotificationVH holder = (NotificationVH) h;
            
            if(holder != null){
                if(notificationCallbacks != null){
                    holder.itemView.setOnClickListener(
                            v -> notificationCallbacks.onNotificationClicked(position , notificationItem)
                    );

                    holder.delete.setOnClickListener(
                            v -> notificationCallbacks.onNotificationDeleted(position , notificationItem)
                    );
                }

                holder.msg.setText(notificationItem.getBody());
                holder.date.setText(TimeUtils.getFromWhen(notificationItem.getTimeStamp(), context));

                if(notificationItem.isRead()){
                    holder.bullet.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.grey_500,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.deleteIcon.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.grey_500,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.dateIcon.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.grey_500,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.msg.setTextColor(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.grey_600,
                                    null
                            )
                    );
                } else{
                    holder.bullet.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.iconColor,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.dateIcon.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.iconColor,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.deleteIcon.setColorFilter(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.black,
                                    null
                            ),
                            PorterDuff.Mode.SRC_IN
                    );
                    holder.msg.setTextColor(
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.black,
                                    null
                            )
                    );
                }
            }
        }
    }


}
