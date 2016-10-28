package com.seki.noteasklite.DataUtil.Bean;

import java.util.List;

/**
 * Created by tianyuan on 16/10/27.
 */
public class OldNotificationsBean extends  WonderFull{
    public List<NotificationEntityBean> notificationsList;
    public static class NotificationEntityBean{
        public String question_id;
        public String question_abstract;
        public String other_side_user_real_name;
        public String other_side_user_id;
        public String key_abstract;
        public String answer_id;
        public String notification_data;
        public int notify_history_type;
    }
}
