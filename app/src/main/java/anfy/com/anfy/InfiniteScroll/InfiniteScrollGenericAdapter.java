package anfy.com.anfy.InfiniteScroll;


import android.content.Context;
import android.os.Looper;

import java.util.ArrayList;

import anfy.com.anfy.Adapter.GenericAdapter;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Interface.GenericItemClickCallback;

/**
 * Created by Ahmed Naeem on 1/5/2018.
 */

public abstract class InfiniteScrollGenericAdapter<T> extends GenericAdapter<T> {


    public InfiniteScrollGenericAdapter(ArrayList<T> items,
                                        GenericItemClickCallback<T> adapterItemClickCallbacks)
    {
        super(items, adapterItemClickCallbacks);
    }

    public void addItem(T item)
    {
        ArrayList<T> items = getItems();
        if(items == null){
            items = new ArrayList<>();
        }

        items.add(0, item);
        notifyItemInserted(0);
    }


    public void addItemsToBottom(final ArrayList<T> newItems){
        if(newItems != null && !newItems.isEmpty()){
            AppController.getExecutorService()
                    .submit(
                            new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<T> items = getItems();
                                    try {
                                        items.addAll(0, newItems);
                                        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                                        handler.post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        notifyItemRangeInserted(0, newItems.size());
                                                    }
                                                }
                                        );
                                    }
                                    catch (Exception ignored){}
                                }
                            }
                    );
        }
    }

    public void addItemsToTop(final ArrayList<T> newItems){
        if(newItems != null && !newItems.isEmpty()){
            AppController.getExecutorService()
                    .submit(
                            new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<T> items = getItems();
                                    try {
                                        final int start = items.size();
                                        items.addAll(newItems);
                                        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                                        handler.post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        notifyItemRangeInserted(start, newItems.size());
                                                    }
                                                }
                                        );
                                    }
                                    catch (Exception ignored){}
                                }
                            }
                    );
        }
    }
}
