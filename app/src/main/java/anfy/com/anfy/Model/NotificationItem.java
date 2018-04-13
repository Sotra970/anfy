package anfy.com.anfy.Model;

/**
 * Created by Ahmed on 8/18/2017.
 */

public class NotificationItem {

    private String message;
    private String time;
    private boolean isRead;

    public NotificationItem(String message, String time, boolean isRead) {
        this.message = message;
        this.time = time;
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public boolean isRead() {
        return isRead;
    }
}
