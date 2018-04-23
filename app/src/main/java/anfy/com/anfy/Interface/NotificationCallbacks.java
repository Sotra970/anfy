package anfy.com.anfy.Interface;

import anfy.com.anfy.Model.NotificationItem;

public interface NotificationCallbacks {

    void onNotificationClicked(int pos , NotificationItem notificationItem);
    void onNotificationDeleted(int  pos , NotificationItem notificationItem);
}
