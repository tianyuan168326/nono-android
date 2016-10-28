package com.seki.noteasklite.DataUtil.BusEvent;

/**
 * Created by tianyuan on 16/10/28.
 */
public  class RefreshCommentEvent{
    String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RefreshCommentEvent(String comment) {
        this.comment = comment;
    }
}
