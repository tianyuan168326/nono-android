package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.Bean.OldNotificationsBean;

import java.util.List;

/**
 * Created by tianyuan on 16/10/27.
 */
public class GetOldNotificationsDoneEvent {
    public List<OldNotificationsBean.NotificationEntityBean> notificationsList;
    public GetOldNotificationsDoneEvent(List<OldNotificationsBean.NotificationEntityBean> notificationsList) {
        this.notificationsList = notificationsList;
    }
}
