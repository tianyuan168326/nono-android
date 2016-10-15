package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;

import java.util.List;

/**
 * Created by yuan on 2016/6/9.
 */
public class NoteLabelSearchDoneEvent {
    public  List<NoteLabelBean> labels;
    public NoteLabelSearchDoneEvent(List<NoteLabelBean> labels) {
        this.labels  =labels;
    }
}
