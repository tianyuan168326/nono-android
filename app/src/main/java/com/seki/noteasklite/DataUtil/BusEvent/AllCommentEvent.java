package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.Bean.AllCommentListBean;

/**
 * Created by tianyuan on 16/10/26.
 */
public class AllCommentEvent {
    public AllCommentListBean c;
    public AllCommentEvent(AllCommentListBean commentList) {
        c = commentList;
    }
}
