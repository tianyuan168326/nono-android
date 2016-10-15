package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;

/**
 * Created by yuan on 2016/6/13.
 */
public class AddNewLabelEvent {
    public NoteLabelBean noteLabelBean;

    public AddNewLabelEvent(NoteLabelBean noteLabelBean) {
        this.noteLabelBean = noteLabelBean;
    }
}
