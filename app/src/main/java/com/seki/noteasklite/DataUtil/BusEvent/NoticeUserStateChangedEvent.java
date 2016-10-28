package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by tianyuan on 16/10/27.
 */
public class NoticeUserStateChangedEvent {
    public String userId;
    public boolean noticeState;
    public NoticeUserStateChangedEvent(String other_user_id, boolean nowNoticeState) {
        userId = other_user_id;
        noticeState = nowNoticeState;
    }
}
