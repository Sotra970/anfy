package anfy.com.anfy.Interface;

import anfy.com.anfy.Model.NotificationItem;

public interface NotificationCallbacks {

    void onNotificationClicked(NotificationItem notificationItem);
    void onNotificationDeleted(NotificationItem notificationItem);
}
