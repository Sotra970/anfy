package anfy.com.anfy.Model;

import android.text.TextUtils;

import anfy.com.anfy.App.AppController;

public class ConsultChatItem {

    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_TEXT = 0;

    private String text;
    private long time_stamp;
    private int type;
    private int user_id;

    public ConsultChatItem() {
    }

    public ConsultChatItem(String text, long time_stamp, int type, int user_id) {
        this.text = text;
        this.time_stamp = time_stamp;
        this.type = type;
        this.user_id = user_id;
    }

    public  boolean isMe(){
        return user_id ==AppController.getUserId() ? true : false ;
    }
    public String getText() {
        return text;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public int getType() {
        return type;
    }

    public int getUser_id() {
        return user_id;
    }
}
